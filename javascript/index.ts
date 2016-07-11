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

import * as readline from 'readline';
import * as fs from 'fs';

class CsvMaxValuesCalculator {

    private maxValues: number[];

    public calculateCsvMaxValues(args: string[]) {
        let filename: string = args[0];
        if (!filename) {
            console.log('Execute with argument <file.csv>');
            process.exit(1);
        }

        console.log('Determining max column values of ' + filename);
        let inputStream: fs.ReadStream = fs.createReadStream(filename);
        inputStream.on('error', (error: any) => {
            console.log('Could not read file ' + filename + ' Reason: ' + error.message);
            process.exit(1);
        })

        const reader: readline.ReadLine = readline.createInterface({
            input: inputStream
        });

        this.findMaxValues(reader);
        reader.on('close', () => this.outputMaxValues());
    }

    private findMaxValues(reader: readline.ReadLine) {
        reader.on('line', (line: string) => {
            console.log('Read line: ' + line);
            let lineItems: string[] = line.split(';');
            let lineValues: number[] = new Array(lineItems.length);
            if (!this.maxValues) {
                this.maxValues = lineValues;
            }
            else if (this.maxValues.length != lineValues.length) {
                console.log('Line has not ' + this.maxValues.length + 'items: ' + line);
                process.exit(1);
            }

            lineItems.forEach((lineItem: string, index: number) => {
                lineValues[index] = parseInt(lineItem, 10);
                if (lineValues[index] > this.maxValues[index]) {
                    this.maxValues[index] = lineValues[index];
                    console.log('New max ' + this.maxValues[index] + ' in column ' + index);
                }
            });
        });
    }

    private outputMaxValues(): void {        
        let separator: string = '';
        let maxValuesString: string = '';
        this.maxValues.forEach((value: number, index: number) => {
            maxValuesString += separator + value;
            separator = ';';
        });
        console.log('The max column values are: ' + maxValuesString);
    }

}

new CsvMaxValuesCalculator().calculateCsvMaxValues(process.argv.slice(2));