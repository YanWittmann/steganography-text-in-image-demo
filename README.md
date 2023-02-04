# steganography-text-in-image-demo

A small java tool that allows for hiding messages in images.

## Usage:

Build:

```bash
mvn clean package
```

Run:

```bash
cd target
java -jar steganography-text-in-image-demo-1.0-SNAPSHOT-jar-with-dependencies.jar encode -f "initial.png" -t Your text to hide -o "encoded.png"
java -jar steganography-text-in-image-demo-1.0-SNAPSHOT-jar-with-dependencies.jar decode -f "encoded.png"
java -jar steganography-text-in-image-demo-1.0-SNAPSHOT-jar-with-dependencies.jar has -f "encoded.png"
```
