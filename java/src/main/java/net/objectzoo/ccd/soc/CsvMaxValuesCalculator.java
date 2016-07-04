/*
 * The MIT License
 *
 * Copyright (C) 2011 Tilmann Kuhn
 *
 * http://www.object-zoo.net
 *
 * mailto:ccd-training@object-zoo.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.objectzoo.ccd.soc;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by tilmann on 06.06.2016.
 */
public class CsvMaxValuesCalculator
{
    private int[] maxValues;

    public void calculateCsvMaxValues(String[] args)
    {
        String filename = args[0];
        if (filename == null || filename.isEmpty())
        {
            System.out.println("Execcute with argument <file.csv>\"");
            System.exit(1);
        }
        System.out.println("Determining max column values of " + filename);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename))))
        {
            findMaxValues(reader);
            outputMaxValues();

        }
        catch (IOException e)
        {
            System.out.println("Could not read file " + filename + " Reason: " + e.getMessage());
        }
    }

    private void findMaxValues(BufferedReader reader) throws IOException
    {
        for (String line = reader.readLine(); line != null; line = reader.readLine())
        {
            System.out.println("Read line: " + line);
            String[] lineItems = line.split(";");
            int[] lineValues = new int[lineItems.length];
            if (maxValues == null)
            {
                maxValues = lineValues;
            }
            else if (maxValues.length != lineValues.length)
            {
                System.out.println("Line has not " + maxValues.length + "items: " + line);
                System.exit(1);
            }
            for (int i = 0; i < lineItems.length; i++)
            {
                lineValues[i] = Integer.valueOf(lineItems[i]);
                if (lineValues[i] > maxValues[i])
                {
                    maxValues[i] = lineValues[i];
                    System.out.println("New max " + maxValues[i] + " in column " + i);
                }
            }
        }
    }

    private void outputMaxValues()
    {
        String separator = "";
        String maxValuesString = "";
        for (int i = 0; i < maxValues.length; i++)
        {
            maxValuesString += separator + maxValues[i];
            separator = ";";
        }
        System.out.println("The max column values are: " + maxValuesString);
    }

    public static void main(String[] args)
    {
        new CsvMaxValuesCalculator().calculateCsvMaxValues(args);
    }
}
