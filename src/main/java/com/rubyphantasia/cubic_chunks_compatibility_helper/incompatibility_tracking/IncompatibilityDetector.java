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

import java.io.FileWriter;
import java.io.IOException;

public class IncompatibilityDetector implements IClassTransformer {

    private static final String[] EXCLUDE_PACKAGES = {"io.github.opencubicchunks", ModInfo.packageName};

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

    private void scanClass(String name, String transformedName, byte[] classData) {
        ClassReader classReader = new ClassReader(classData);
        IncompatibilityDetectorClassVisitor incompatibilityDetector = new IncompatibilityDetectorClassVisitor(name, transformedName, this);
        classReader.accept(incompatibilityDetector, 0);
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
//            if (!name.contains("ModLogger")) {
//                ModLogger.debug("Scanning class " + name + " for common CubicChunks incompatibilities.");
//            }
                scanClass(name, transformedName, basicClass);
            } else {
                ModLogger.debug("Encountered class with null data: " + name);
            }
        }
        return basicClass;
    }
}
