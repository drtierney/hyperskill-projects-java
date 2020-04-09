package encryptdecrypt;

import encryptdecrypt.crypto.EncoderFactory;
import encryptdecrypt.crypto.IEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

    class Task {
        private String input = "";
        private String algorithm = "";
        private String encode;
        private String data  = "";
        private int key = 0;
        private String inFile = "";
        private String inFileData = "";
        private String outFile = "";

        private String output;

        public void run(String[] args) throws IOException {
            argsParse(args);
            processInput();
            processEncodeAndOutput();
        }

        private void argsParse(String[] args){
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-mode":
                        encode = args[i + 1];
                        break;
                    case "-data":
                        data = args[i + 1];
                        break;
                    case "-alg":
                        algorithm = args[i + 1];
                        break;
                    case "-key":
                        key = Integer.parseInt(args[i + 1]);
                        break;
                    case "-in":
                        inFile = args[i + 1];
                        break;
                    case "-out":
                        outFile = args[i + 1];
                }
            }
        }

        private void processInput(){
            // Check if inFile exists
            if (!this.inFile.isBlank()) {
                try (Scanner scanner = new Scanner(new File(this.inFile))) {
                    this.inFileData = scanner.nextLine();
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                }
            }
            // if -data and -in both used, prioritise -data
            this.input = !this.data.equals("") ? data : inFileData;
        }

        private void processEncodeAndOutput(){
            final EncoderFactory encoderFactory = new EncoderFactory();
            final IEncoder encoder = encoderFactory.create(algorithm);

            if(encode.equals("enc")){
                output = encoder.encode(input, key);
            } else {
                output = encoder.decode(input, key);
            }
            //Check if -out file has been included, if not go to std out
            if (outFile.isBlank()) {
                System.out.println(output);
            } else {
                try (FileWriter fileWriter = new FileWriter(new File(outFile))) {
                    fileWriter.write(output);
                    fileWriter.flush();
                } catch (IOException e) {
                    System.out.println("Error writing result");
                }
            }
        }
    }
