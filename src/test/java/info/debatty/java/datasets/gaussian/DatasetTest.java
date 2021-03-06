/*
 * The MIT License
 *
 * Copyright 2016 Thibault Debatty.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package info.debatty.java.datasets.gaussian;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import junit.framework.TestCase;

/**
 *
 * @author Thibault Debatty
 */
public class DatasetTest extends TestCase {
    public static final int DIMENSIONALITY = 20;
    public static final int CENTERS = 13;
    public static final int SIZE = 100;

    /**
     * Test of addCenter method, of class Dataset.
     */
    public void testBuilder() {
        Dataset dataset = new Dataset.Builder(DIMENSIONALITY, CENTERS)
                .setOverlap(Dataset.Builder.Overlap.MEDIUM)
                .setSize(SIZE)
                .build();

        for (double[] point : dataset) {
            System.out.print(".");
        }
    }

    public void testSaveLoad() throws IOException, ClassNotFoundException {
        Dataset dataset = new Dataset.Builder(DIMENSIONALITY, CENTERS)
                .setSize(SIZE)
                .build();

        File file = File.createTempFile("testfile", "ser");
        dataset.save(new FileOutputStream(file));

        Dataset d2 = (Dataset) Dataset.load(new FileInputStream(file));
        assertEquals(dataset, d2);
    }


}
