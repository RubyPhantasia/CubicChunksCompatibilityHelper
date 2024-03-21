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

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class IncompatibilityDetectorClassVisitor extends ClassVisitor {
    public final String name;
    public final String transformedName;
    private String obfuscatedName;
    private final IncompatibilityDetector incompatibilityDetector;
    public IncompatibilityDetectorClassVisitor(String name, String transformedName, IncompatibilityDetector incompatibilityDetector) {
        super(Opcodes.ASM5);
        this.name = name;
        this.transformedName = transformedName;
        this.incompatibilityDetector = incompatibilityDetector;
    }

    private boolean containsInterface(String interfaceToCheck, String[] interfaces) {
        for (String intface : interfaces) {
            if (interfaceToCheck.equals(intface)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void visit(int version, int access, String obfuscatedName, String signature, String superName, String[] interfaces) {
        super.visit(version, access, obfuscatedName, signature, superName, interfaces);
        this.obfuscatedName = obfuscatedName;
        if (superName.startsWith("net/minecraft/world/gen/MapGen")) {
            incompatibilityDetector.writeLine("Found class that extends a net/minecraft/world/gen/MapGen* class: "+this);
        }
        if (containsInterface("net/minecraftforge/fml/common/IWorldGenerator", interfaces)) {
            incompatibilityDetector.writeLine("Found class that implements net/minecraftforge/fml/common/IWorldGenerator: "+this);
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new IncompatibilityDetectorMethodVisitor(this, name, desc, signature, incompatibilityDetector);
    }

    @Override
    public String toString() {
        return name+"(aka "+transformedName+" or "+obfuscatedName+")";
    }
}
