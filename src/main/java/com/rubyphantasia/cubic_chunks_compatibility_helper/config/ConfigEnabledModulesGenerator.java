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
