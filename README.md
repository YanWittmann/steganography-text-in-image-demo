# steganography-text-in-image-demo

A small java tool that allows for hiding messages in images.

## Usage:

```
encode -f <file> -o <output> -t <text>
decode -f <file> [--force]
has -f <file>
```

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

Replace initial.png with the path to your image and encoded.png with the path to the image you want to create.
