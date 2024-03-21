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

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class IncompatibilityDetectorMethodVisitor extends MethodVisitor {
    private final IncompatibilityDetectorClassVisitor ownerClass;
    public final String name;
    public final String description;
    public final String signature;

    private final IncompatibilityDetector incompatibilityDetector;
    public IncompatibilityDetectorMethodVisitor(IncompatibilityDetectorClassVisitor ownerClass, String name, String desc, String signature, IncompatibilityDetector incompatibilityDetector) {
        super(Opcodes.ASM5);
        this.ownerClass = ownerClass;
        this.name = name;
        this.description = desc;
        this.signature = signature;
        this.incompatibilityDetector = incompatibilityDetector;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);
        if (owner.equals("net/minecraft/util/math/BlockPos")) {
            if (name.equals("func_177969_a") && desc.equals("(J)Lnet/minecraft/util/math/BlockPos;")) {
                incompatibilityDetector.writeLineWithSubPoints("Found method using BlockPos.fromLong:", ""+this, "in " + ownerClass);
            } else if (name.equals("func_177986_g") && desc.equals("()J")) {
                incompatibilityDetector.writeLineWithSubPoints("Found method using BlockPos.toLong:", ""+this,"in "+ownerClass);
            }
        }
    }

    @Override
    public String toString() {
        return name+description+signature;
    }
}
