package com.rubyphantasia.cubic_chunks_compatibility_helper.config;

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModInfo;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.ModuleInfo;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public class ConfigEnabledModulesGenerator implements IClassTransformer {

    public byte[] transformConfigClass(byte[] originalClass) {
        // Copy class
        ClassReader classReader = new ClassReader(originalClass);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);

        for (ModuleInfo moduleInfo : ModuleInfo.values()) {
            AnnotationVisitor annotationVisitor = classWriter.visitField(Opcodes.ACC_STATIC + Opcodes.ACC_PUBLIC,
                            getModuleEnabledFieldName(moduleInfo), "Z", null, moduleInfo.defaultEnabled)
                    .visitAnnotation("Lnet/minecraftforge/common/config/Config$Comment;", true);
            AnnotationVisitor arrayVisitor = annotationVisitor.visitArray("value");
            arrayVisitor.visit("", moduleInfo.configDescription);
            arrayVisitor.visitEnd();
            annotationVisitor.visitEnd();
        }

        return classWriter.toByteArray();
    }
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals(ModInfo.packageName+".config.ConfigEnabledModules")) {
            return transformConfigClass(basicClass);
        } else {
            return basicClass;
        }
    }

    public static String getModuleEnabledFieldName(ModuleInfo moduleInfo) {
        return "enabled_"+moduleInfo.getLowerName();
    }
}
