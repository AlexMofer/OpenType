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

import com.am.font.opentype.OpenTypeReader;
import com.am.font.opentype.TableRecord;

import java.util.Objects;

/**
 * Horizontal Header Table
 * This table contains information for horizontal layout. The values in the minRightSidebearing,
 * minLeftSideBearing and xMaxExtent should be computed using only glyphs that have contours.
 * Glyphs with no contours should be ignored for the purposes of these calculations.
 * All reserved areas must be set to 0.
 */
@SuppressWarnings("unused")
public class HorizontalHeaderTable extends BaseTable {

    private final int mMajorVersion;
    private final int mMinorVersion;
    private final int mAscender;
    private final int mDescender;
    private final int mLineGap;
    private final int mAdvanceWidthMax;
    private final int mMinLeftSideBearing;
    private final int mMinRightSideBearing;
    private final int mXMaxExtent;
    private final int mCaretSlopeRise;
    private final int mCaretSlopeRun;
    private final int mCaretOffset;
    private final int mMetricDataFormat;
    private final int mNumberOfHMetrics;

    public HorizontalHeaderTable(OpenTypeReader reader, TableRecord record) throws IOException {
        super(record);
        if (reader == null || record == null || record.getTableTag() != TableRecord.TAG_HHEA)
            throw new IOException();
        reader.seek(record.getOffset());
        final int majorVersion = reader.readUnsignedShort();
        final int minorVersion = reader.readUnsignedShort();
        final int ascender = reader.readShort();
        final int descender = reader.readShort();
        final int lineGap = reader.readShort();
        final int advanceWidthMax = reader.readUnsignedShort();
        final int minLeftSideBearing = reader.readShort();
        final int minRightSideBearing = reader.readShort();
        final int xMaxExtent = reader.readShort();
        final int caretSlopeRise = reader.readShort();
        final int caretSlopeRun = reader.readShort();
        final int caretOffset = reader.readShort();
        reader.skip(8);// reserved
        final int metricDataFormat = reader.readShort();
        final int numberOfHMetrics = reader.readUnsignedShort();

        mMajorVersion = majorVersion;
        mMinorVersion = minorVersion;
        mAscender = ascender;
        mDescender = descender;
        mLineGap = lineGap;
        mAdvanceWidthMax = advanceWidthMax;
        mMinLeftSideBearing = minLeftSideBearing;
        mMinRightSideBearing = minRightSideBearing;
        mXMaxExtent = xMaxExtent;
        mCaretSlopeRise = caretSlopeRise;
        mCaretSlopeRun = caretSlopeRun;
        mCaretOffset = caretOffset;
        mMetricDataFormat = metricDataFormat;
        mNumberOfHMetrics = numberOfHMetrics;
    }

    /**
     * Major version number of the horizontal header table — set to 1.
     *
     * @return Major version number.
     */
    public int getMajorVersion() {
        return mMajorVersion;
    }

    /**
     * Minor version number of the horizontal header table — set to 0.
     *
     * @return Minor version number.
     */
    public int getMinorVersion() {
        return mMinorVersion;
    }

    /**
     * Typographic ascent (Distance from baseline of highest ascender
     * http://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6hhea.html).
     *
     * @return Typographic ascent.
     */
    public int getAscender() {
        return mAscender;
    }

    /**
     * Typographic descent (Distance from baseline of lowest descender
     * http://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6hhea.html).
     *
     * @return Typographic descent
     */
    public int getDescender() {
        return mDescender;
    }

    /**
     * Typographic line gap.
     * Negative LineGap values are treated as zero in some legacy platform implementations.
     *
     * @return Typographic line gap.
     */
    public int getLineGap() {
        return mLineGap;
    }

    /**
     * Maximum advance width value in 'hmtx' table.
     *
     * @return Maximum advance width.
     */
    public int getAdvanceWidthMax() {
        return mAdvanceWidthMax;
    }

    /**
     * Minimum left sidebearing value in 'hmtx' table.
     *
     * @return Minimum left sidebearing.
     */
    public int getMinLeftSideBearing() {
        return mMinLeftSideBearing;
    }

    /**
     * Minimum right sidebearing value; calculated as Min(aw - lsb - (xMax - xMin)).
     *
     * @return Minimum right sidebearing.
     */
    public int getMinRightSideBearing() {
        return mMinRightSideBearing;
    }

    /**
     * Max(lsb + (xMax - xMin)).
     *
     * @return xMax extent.
     */
    public int getXMaxExtent() {
        return mXMaxExtent;
    }

    /**
     * Used to calculate the slope of the cursor (rise/run); 1 for vertical.
     *
     * @return Caret slope rise.
     */
    public int getCaretSlopeRise() {
        return mCaretSlopeRise;
    }

    /**
     * 0 for vertical.
     *
     * @return Caret slope run.
     */
    public int getCaretSlopeRun() {
        return mCaretSlopeRun;
    }

    /**
     * The amount by which a slanted highlight on a glyph needs to be shifted to produce
     * the best appearance. Set to 0 for non-slanted fonts.
     *
     * @return Caret offset.
     */
    public int getCaretOffset() {
        return mCaretOffset;
    }

    /**
     * 0 for current format.
     *
     * @return Metric data format.
     */
    public int getMetricDataFormat() {
        return mMetricDataFormat;
    }

    /**
     * Number of hMetric entries in 'hmtx' table
     *
     * @return Number of hMetric entries.
     */
    public int getNumberOfHMetrics() {
        return mNumberOfHMetrics;
    }

    @Override
    public int getHashCode() {
        return Objects.hash(super.getHashCode(), mMajorVersion, mMinorVersion, mAscender,
                mDescender, mLineGap, mAdvanceWidthMax, mMinLeftSideBearing, mMinRightSideBearing,
                mXMaxExtent, mCaretSlopeRise, mCaretSlopeRun, mCaretOffset, mMetricDataFormat,
                mNumberOfHMetrics);
    }

    @Override
    public String getString() {
        return "HorizontalHeaderTable{" +
                "record=" + String.valueOf(getTableRecord()) +
                ", majorVersion=" + mMajorVersion +
                ", minorVersion=" + mMinorVersion +
                ", ascender=" + mAscender +
                ", descender=" + mDescender +
                ", lineGap=" + mLineGap +
                ", advanceWidthMax=" + mAdvanceWidthMax +
                ", minLeftSideBearing=" + mMinLeftSideBearing +
                ", minRightSideBearing=" + mMinRightSideBearing +
                ", xMaxExtent=" + mXMaxExtent +
                ", caretSlopeRise=" + mCaretSlopeRise +
                ", caretSlopeRun=" + mCaretSlopeRun +
                ", caretOffset=" + mCaretOffset +
                ", metricDataFormat=" + mMetricDataFormat +
                ", numberOfHMetrics=" + mNumberOfHMetrics +
                '}';
    }
}
