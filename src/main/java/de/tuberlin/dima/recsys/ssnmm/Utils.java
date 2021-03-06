/*
 * Copyright (C) 2012 Sebastian Schelter <sebastian.schelter [at] tu-berlin.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package de.tuberlin.dima.recsys.ssnmm;

import com.google.common.base.Charsets;
import com.google.common.collect.AbstractIterator;
import com.google.common.io.Closeables;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * Helper methods for reading files
 */
public class Utils {
  
  private Utils() {}

  public static Iterable<String> readLines(File file) {
    return new FileLineIterable(file);
  }
  
  static class FileLineIterable implements Iterable<String> {

    private final File file;

    FileLineIterable(File file) {
      this.file = file;
    }

    @Override
    public Iterator<String> iterator() {
      try {
        return new FileLineIterator(file, Charsets.UTF_8);
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }
  }
  
  static class FileLineIterator extends AbstractIterator<String> implements Closeable {

    private final BufferedReader reader;

    FileLineIterator(File file, Charset encoding) throws IOException {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
    }

    @Override
    protected String computeNext() {
      String line;
      try {
        line = reader.readLine();
      } catch (IOException ioe) {
        close();
        throw new IllegalStateException(ioe);
      }
      return line == null ? endOfData() : line;
    }

    @Override
    public void close() {
      endOfData();
      Closeables.closeQuietly(reader);
    }

  }

}