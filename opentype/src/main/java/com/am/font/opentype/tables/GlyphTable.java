/*
 * Copyright (C) 2018 AlexMofer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.am.font.opentype.tables;

import java.io.IOException;
import java.util.Arrays;

import com.am.font.opentype.OpenTypeReader;
import com.am.font.opentype.TableRecord;

import java.util.Objects;

/**
 * Glyph Data
 * This table contains information that describes the glyphs in the font in the TrueType outline
 * format. Information regarding the rasterizer (scaler) refers to the TrueType rasterizer.
 * The 'glyf' table is comprised of a list of glyph data blocks, each of which provides
 * the description for a single glyph. Glyphs are referenced by identifiers (glyph IDs),
 * which are sequential integers beginning at zero. The total number of glyphs is specified
 * by the numGlyphs field in the 'maxp' table. The 'glyf' table does not include any overall
 * table header or records providing offsets to glyph data blocks. Rather, the 'loca' table
 * provides an array of offsets, indexed by glyph IDs, which provide the location of each
 * glyph data block within the 'glyf' table. Note that the 'glyf' table must always be used
 * in conjunction with the 'loca' and 'maxp' tables. The size of each glyph data block is
 * inferred from the difference between two consecutive offsets in the 'loca' table
 * (with one extra offset provided to give the size of the last glyph data block).
 * As a result of the 'loca' format, glyph data blocks within the 'glyf' table must be
 * in glyph ID order.
 */
@SuppressWarnings("unused")
public class GlyphTable extends BaseTable {

    private final int mNumberOfContours;
    private final int mXMin;
    private final int mYMin;
    private final int mXMax;
    private final int mYMax;
    private final Object mGlyphDescription;

    public GlyphTable(OpenTypeReader reader, TableRecord record) throws IOException {
        super(record);
        if (reader == null || record == null || record.getTableTag() != TableRecord.TAG_GLYF)
            throw new IOException();
        reader.seek(record.getOffset());
        final int numberOfContours = reader.readShort();
        final int xMin = reader.readShort();
        final int yMin = reader.readShort();
        final int xMax = reader.readShort();
        final int yMax = reader.readShort();
        final Object glyphDescription;
        if (numberOfContours >= 0) {
            // Simple Glyph Description
            final int[] endPtsOfContours = new int[numberOfContours];
            for (int i = 0; i < endPtsOfContours.length; i++) {
                endPtsOfContours[i] = reader.readUnsignedShort();
            }
            final int instructionLength = reader.readUnsignedShort();
            final int[] instructions = new int[instructionLength];
            for (int i = 0; i < instructions.length; i++) {
                instructions[i] = reader.readUnsignedByte();
            }
            // TODO: 2018/9/27 flags xCoordinates yCoordinates
            // uint8            flags[variable]          Array of flag elements.
            // uint8 or int16   xCoordinates[variable]   Contour point x-coordinates.
            // uint8 or int16   yCoordinates[variable]   Contour point y-coordinates.
            glyphDescription = new SimpleGlyphDescription(endPtsOfContours, instructionLength,
                    instructions, null, null, null);
        } else {
            // Composite Glyph Description
            final int flags = reader.readUnsignedShort();
            final int glyphIndex = reader.readUnsignedShort();
            // TODO: 2018/9/27  argument1 argument2 and so on
            // uint8, int8, uint16 or int16   argument1   x-offset for component or point number.
            // uint8, int8, uint16 or int16   argument2   y-offset for component or point number.
            glyphDescription = new CompositeGlyphDescription(flags, glyphIndex,
                    0, 0);
        }
        mNumberOfContours = numberOfContours;
        mXMin = xMin;
        mYMin = yMin;
        mXMax = xMax;
        mYMax = yMax;
        mGlyphDescription = glyphDescription;
    }

    /**
     * If the number of contours is greater than or equal to zero, this is a simple glyph.
     * If negative, this is a composite glyph — the value -1 should be used for composite glyphs.
     *
     * @return The number of contours.
     */
    public int getNumberOfContours() {
        return mNumberOfContours;
    }

    /**
     * Minimum x for coordinate data.
     *
     * @return Minimum x for coordinate data.
     */
    public int getXMin() {
        return mXMin;
    }

    /**
     * Minimum y for coordinate data.
     *
     * @return Minimum y for coordinate data.
     */
    public int getYMin() {
        return mYMin;
    }

    /**
     * Maximum x for coordinate data.
     *
     * @return Maximum x for coordinate data.
     */
    public int getXMax() {
        return mXMax;
    }

    /**
     * Maximum y for coordinate data.
     *
     * @return Maximum y for coordinate data.
     */
    public int getYMax() {
        return mYMax;
    }

    /**
     * Glyph Description
     *
     * @return Glyph Description
     */
    public Object getGlyphDescription() {
        return mGlyphDescription;
    }

    @Override
    public int getHashCode() {
        return Objects.hash(super.getHashCode(), mNumberOfContours, mXMin, mYMin, mXMax, mYMax,
                mGlyphDescription);
    }

    @Override
    public String getString() {
        return "GlyphTable{" +
                "record=" + String.valueOf(getTableRecord()) +
                ", numberOfContours=" + mNumberOfContours +
                ", xMin=" + mXMin +
                ", yMin=" + mYMin +
                ", xMax=" + mXMax +
                ", yMax=" + mYMax +
                ", glyphDescription=" + String.valueOf(mGlyphDescription) +
                '}';
    }

    /**
     * Simple Glyph Description
     */
    public static class SimpleGlyphDescription {

        private final int[] mEndPtsOfContours;
        private final int mInstructionLength;
        private final int[] mInstructions;
        private final int[] mFlags;
        private final int[] mXCoordinates;
        private final int[] mYCoordinates;

        @SuppressWarnings("WeakerAccess")
        public SimpleGlyphDescription(int[] endPtsOfContours, int instructionLength,
                                      int[] instructions,
                                      int[] flags, int[] xCoordinates, int[] yCoordinates) {
            mEndPtsOfContours = endPtsOfContours;
            mInstructionLength = instructionLength;
            mInstructions = instructions;
            mFlags = flags;
            mXCoordinates = xCoordinates;
            mYCoordinates = yCoordinates;
        }

        /**
         * Array of point indices for the last point of each contour, in increasing numeric order.
         *
         * @return Array of point indices for the last point of each contour.
         */
        public int[] getEndPtsOfContours() {
            return mEndPtsOfContours;
        }

        /**
         * Total number of bytes for instructions. If instructionLength is zero,
         * no instructions are present for this glyph, and this field is followed directly
         * by the flags field.
         *
         * @return Total number of bytes for instructions.
         */
        public int getInstructionLength() {
            return mInstructionLength;
        }

        /**
         * Array of instruction byte code for the glyph.
         *
         * @return Instructions.
         */
        public int[] getInstructions() {
            return mInstructions;
        }

        /**
         * Array of flag elements.
         *
         * @return Flags.
         */
        public int[] getFlags() {
            return mFlags;
        }

        /**
         * Contour point x-coordinates. Coordinate for the first point is relative to (0,0);
         * others are relative to previous point.
         *
         * @return Contour point x-coordinates.
         */
        public int[] getXCoordinates() {
            return mXCoordinates;
        }

        /**
         * Contour point y-coordinates. Coordinate for the first point is relative to (0,0);
         * others are relative to previous point.
         *
         * @return Contour point y-coordinates.
         */
        public int[] getYCoordinates() {
            return mYCoordinates;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SimpleGlyphDescription that = (SimpleGlyphDescription) o;
            return mInstructionLength == that.mInstructionLength &&
                    Arrays.equals(mEndPtsOfContours, that.mEndPtsOfContours) &&
                    Arrays.equals(mInstructions, that.mInstructions) &&
                    Arrays.equals(mFlags, that.mFlags) &&
                    Arrays.equals(mXCoordinates, that.mXCoordinates) &&
                    Arrays.equals(mYCoordinates, that.mYCoordinates);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(mInstructionLength);
            result = 31 * result + Arrays.hashCode(mEndPtsOfContours);
            result = 31 * result + Arrays.hashCode(mInstructions);
            result = 31 * result + Arrays.hashCode(mFlags);
            result = 31 * result + Arrays.hashCode(mXCoordinates);
            result = 31 * result + Arrays.hashCode(mYCoordinates);
            return result;
        }

        @Override
        public String toString() {
            return "SimpleGlyphDescription{" +
                    "endPtsOfContours=" + Arrays.toString(mEndPtsOfContours) +
                    ", instructionLength=" + mInstructionLength +
                    ", instructions=" + Arrays.toString(mInstructions) +
                    ", flags=" + Arrays.toString(mFlags) +
                    ", xCoordinates=" + Arrays.toString(mXCoordinates) +
                    ", yCoordinates=" + Arrays.toString(mYCoordinates) +
                    '}';
        }
    }

    /**
     * Composite Glyph Description
     */
    public static class CompositeGlyphDescription {
        private final int mFlags;
        private final int mGlyphIndex;
        private final int mArgument1;
        private final int mArgument2;

        @SuppressWarnings("WeakerAccess")
        public CompositeGlyphDescription(int flags, int glyphIndex, int argument1, int argument2) {
            mFlags = flags;
            mGlyphIndex = glyphIndex;
            mArgument1 = argument1;
            mArgument2 = argument2;
        }

        /**
         * Component flag
         *
         * @return Component flag.
         */
        public int getFlags() {
            return mFlags;
        }

        /**
         * Glyph index of component
         *
         * @return Glyph index of component.
         */
        public int getGlyphIndex() {
            return mGlyphIndex;
        }

        /**
         * x-offset for component or point number; type depends on bits 0 and 1 in component flags
         *
         * @return x-offset for component or point number.
         */
        public int getArgument1() {
            return mArgument1;
        }

        /**
         * y-offset for component or point number; type depends on bits 0 and 1 in component flags
         *
         * @return y-offset for component or point number.
         */
        public int getArgument2() {
            return mArgument2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CompositeGlyphDescription that = (CompositeGlyphDescription) o;
            return mFlags == that.mFlags &&
                    mGlyphIndex == that.mGlyphIndex &&
                    mArgument1 == that.mArgument1 &&
                    mArgument2 == that.mArgument2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(mFlags, mGlyphIndex, mArgument1, mArgument2);
        }

        @Override
        public String toString() {
            return "CompositeGlyphDescription{" +
                    "flags=" + mFlags +
                    ", glyphIndex=" + mGlyphIndex +
                    ", argument1=" + mArgument1 +
                    ", argument2=" + mArgument2 +
                    '}';
        }
    }
}
