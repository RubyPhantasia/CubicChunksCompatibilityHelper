/*
 * LICENSE:
 *
 * Copyright (C) 2023 RubyPhantasia
 *
 * This file is part of CubicChunksCompatibilityHelper
 *
 * CubicChunksCompatibilityHelper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.rubyphantasia.cubic_chunks_compatibility_helper.incompatibility_tracking;

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModInfo;
import com.rubyphantasia.cubic_chunks_compatibility_helper.ModLogger;
import com.rubyphantasia.cubic_chunks_compatibility_helper.config.ConfigGeneral;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class IncompatibilityDetector implements IClassTransformer {

    private static final String[] EXCLUDE_PACKAGES = {
            "net.minecraft",
            "net.minecraftforge",
            "io.github.opencubicchunks",
            "io.netty",
            ModInfo.packageName
    };

    private static final long[] CONCERNING_CONSTANTS = {
            127,
            128,
            255,
            256
    };

    private static final String[] CONCERNING_VARIABLE_TYPES_RAW = {
            "net/minecraft/world/gen/IChunkGenerator",
            "net/minecraft/world/chunk/IChunkProvider",
            "net/minecraft/world/chunk/ChunkPrimer",
            "net/minecraftforge/event/terraingen/DecorateBiomeEvent",
            "net/minecraftforge/event/terraingen/DecorateBiomeEvent$Decorate",
            "net.minecraft.world.World func_72800_K()I"
    };

    private static final String[] CONCERNING_VARIABLE_TYPES;

    private static final ConcerningMethodsPackage[] CONCERNING_METHODS_PACKAGES = {
            new ConcerningMethodsPackage("net/minecraft/util/math/BlockPos", new MethodIdentifier[] {
                    // long-compressed BlockPos's only allocate 12 bits for the y-value, which is problematic for
                    //  y-values outside the range [-2048, 2048) (-/+ range, as the highest bit is treated as a sign bit).
                    new MethodIdentifier("toLong", "func_177986_g()J"),
                    new MethodIdentifier("fromLong", "func_177969_a(J)Lnet/minecraft/util/math/BlockPos;")
            }),
            new ConcerningMethodsPackage("net/minecraft/world/World", new MethodIdentifier[] {
                    // While this should yield the correct value in a cubic world, it might be used in a loop
                    //  that iterates over y-values in the range [0, getHeight()), which would be a problem in cubic worlds.
                    new MethodIdentifier("getHeight", "func_72800_K()I")
            })
    };

    // Words/phrases that might be associated with incompatible behavior
    private static final String[] CONCERNING_CLASS_NAME_FRAGMENTS = {
            "solar" // Might scan up to the top of the world.
    };

    private final FileWriter incompatibilityWriter;

    public IncompatibilityDetector() {
        try {
            this.incompatibilityWriter = new FileWriter("Incompatibilities.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeLine(String line) {
        try {
            incompatibilityWriter.write(line);
            incompatibilityWriter.write("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeLineWithSubPoints(String line, String ...subpoints) {
        try {
            this.writeLine(line);
            for (String subpoint : subpoints) {
                this.writeLine("\t"+subpoint);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean listContainsString(List<String> list, String str) {
        for (String arrStr : list) {
            if (str.equals(arrStr)) {
                return true;
            }
        }
        return false;
    }

    private String computeMethodLabel(MethodNode methodNode) {
        return methodNode.name+methodNode.desc;
    }

    private void inspectLocalVariables(String className, MethodNode methodNode) {
        String methodLabel = computeMethodLabel(methodNode);
        String inMethod = "In method: "+methodLabel;
        String inClass = "In class: " + className;
        if (methodNode.localVariables != null) {
            for (LocalVariableNode variableNode : methodNode.localVariables) {
                String lowerName = variableNode.name.toLowerCase(Locale.ROOT);
                String variableNameOutput = "Variable/parameter name: " + variableNode.name;
                if (lowerName.equals("chunkx")) {
                    writeLineWithSubPoints("Found method with local variable/parameter named \"chunkx\":",
                            variableNameOutput, inMethod, inClass);
                } else if (lowerName.equals("chunkz")) {
                    writeLineWithSubPoints("Found method with local variable/parameter named \"chunkz\":",
                            variableNameOutput, inMethod, inClass);
                } else if (lowerName.startsWith("chunk")) {
                    writeLineWithSubPoints("Found local variable/parameter name starting with \"chunk\", in method:",
                            variableNameOutput, inMethod, inClass);
                } else if (lowerName.contains("chunk")) {
                    writeLineWithSubPoints("Found local variable/parameter name which includes the string \"chunk\", in method:",
                            variableNameOutput, inMethod, inClass);
                }

                for (String concerningType : CONCERNING_VARIABLE_TYPES) {
                    if (variableNode.desc.equals(concerningType)) {
                        writeLineWithSubPoints("Found method with a local variable/parameter of type \""+concerningType+"\":",
                                variableNameOutput, inMethod, inClass);
                    }
                }
            }
        }
    }

    private void inspectIntegralConstant(long constant, String className, String methodLabel) {
        for (long concerningConstant : CONCERNING_CONSTANTS) {
            if (constant == concerningConstant) {
                writeLineWithSubPoints("Found method using concerning integral constant "+constant+":",
                        "Method: "+methodLabel, "In class: "+className);
                break;
            }
        }
    }

    private void inspectInstructions(String className, MethodNode methodNode) {
        String methodLabel = computeMethodLabel(methodNode);
        for (ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator(); iterator.hasNext(); ) {
            AbstractInsnNode instruction = iterator.next();
            switch (instruction.getType()) {
                case AbstractInsnNode.METHOD_INSN: {
                    MethodInsnNode castInstruction = (MethodInsnNode) instruction;
                    String fullSignature = castInstruction.name + castInstruction.desc;
                    for (ConcerningMethodsPackage methodsPackage : CONCERNING_METHODS_PACKAGES) {
                        if (castInstruction.owner.equals(methodsPackage.owner)) {
                            for (MethodIdentifier method : methodsPackage.methods) {
                                if (fullSignature.equals(method.obfSignature)) {
                                    writeLineWithSubPoints("Found method using " + methodsPackage.owner + "#" + method.deobfName + ":",
                                            "In method: " + methodLabel, "In class: " + className);
                                }
                            }
                        }
                    }
                    break;
                }
                case AbstractInsnNode.INT_INSN: {
                    IntInsnNode castInstruction = (IntInsnNode) instruction;
                    inspectIntegralConstant(castInstruction.operand, className, methodLabel);
                    break;
                }
                case AbstractInsnNode.LDC_INSN: {
                    LdcInsnNode castInstruction = (LdcInsnNode) instruction;
                    Object constant = castInstruction.cst;
                    if (constant instanceof Long) {
                        inspectIntegralConstant((Long) constant, className, methodLabel);
                    } else if (constant instanceof Integer) {
                        inspectIntegralConstant((Integer) constant, className, methodLabel);
                    }
                    break;
                }
            }
        }
    }

    private void inspectMethods(ClassNode classNode) {
        if (classNode.methods != null) {
            for (MethodNode methodNode : classNode.methods) {
                inspectLocalVariables(classNode.name, methodNode);
                inspectInstructions(classNode.name, methodNode);
            }
        }
    }

    private void inspectInheritance(ClassNode classNode) {
        if (classNode.superName.startsWith("net/minecraft/world/gen/MapGen")) {
            writeLineWithSubPoints("Found class that extends a net/minecraft/world/gen/MapGen* class:", classNode.name);
        }
        if (listContainsString(classNode.interfaces, "net/minecraftforge/fml/common/IWorldGenerator")) {
            writeLineWithSubPoints("Found class that implements net/minecraftforge/fml/common/IWorldGenerator:", classNode.name);
        }
    }

    private void inspectClassName(ClassNode classNode) {
        String lowerName = classNode.name.toLowerCase(Locale.ROOT);
        for (String concerningClassNameFragment : CONCERNING_CLASS_NAME_FRAGMENTS) {
            if (lowerName.contains(concerningClassNameFragment)) {
                writeLineWithSubPoints("Found class with the concerning word/phrase "+concerningClassNameFragment+" in its name:",
                        classNode.name);
            }
        }
    }

    private void scanClass(String name, String transformedName, byte[] classData) {
        ClassReader classReader = new ClassReader(classData);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        inspectInheritance(classNode);
        inspectMethods(classNode);
        inspectClassName(classNode);

        try {
            incompatibilityWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean shouldParse(String className) {
        for (String excludePrefix : EXCLUDE_PACKAGES) {
            if (className.startsWith(excludePrefix)) {
                return false;
            }
        }
        return true;
    }
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        // Check config after checking if the class should parse, otherwise we get ClassCircularlityErrors b/c we
        //  check the config class when the config class is being loaded.
        if (shouldParse(name) && ConfigGeneral.scanClassesForIncompatibilities){
            if (basicClass != null) {
                scanClass(name, transformedName, basicClass);
            } else {
                ModLogger.debug("Encountered class with null data: " + name);
            }
        }
        return basicClass;
    }

    static {
//        CONCERNING_PARAMETER_TYPES = (String[]) Arrays.stream(CONCERNING_PARAMETER_TYPES_RAW).map(raw -> "L"+raw+";").toArray();
        CONCERNING_VARIABLE_TYPES = new String[CONCERNING_VARIABLE_TYPES_RAW.length];
        for (int i = 0; i < CONCERNING_VARIABLE_TYPES_RAW.length; i++) {
            CONCERNING_VARIABLE_TYPES[i] = "L"+ CONCERNING_VARIABLE_TYPES_RAW[i]+";";
        }
    }

    private static class ConcerningMethodsPackage {
        private final String owner;
        private final MethodIdentifier[] methods;

        public ConcerningMethodsPackage(String owner, MethodIdentifier[] methods) {
            this.owner = owner;
            this.methods = methods;
        }
    }

    private static class MethodIdentifier {
        private final String deobfName;
        private final String obfSignature;

        public MethodIdentifier(String deobfName, String obfSignature) {
            this.deobfName = deobfName;
            this.obfSignature = obfSignature;
        }
    }
}
