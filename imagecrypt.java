import java.io.*;
import java.nio.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.security.SecureRandom;
import javax.swing.*;


public class imagecrypt {
	public final static BigInteger one = BigInteger.ONE;
	public final static BigInteger MAX = new BigInteger("255");
	private static final SecureRandom random = new SecureRandom();
	
	public static void main(String[] args) throws IOException {
		
		BMPImage fileName = new BMPImage("480-360.bmp");
		
		// Display original image
		SwingUtilities.invokeLater(() -> createAndShowGUI("Original BMP Image", fileName.getName()));
		
		// generating public and private keys
		BigInteger[] publicKeyPrivateKey = generateRSAKeys(30);
		
		// Encrypting BMP Image and storing RGB data
		RGBColor[][] encryptedData = seeBMPImageData(fileName.getName(), publicKeyPrivateKey[0], publicKeyPrivateKey[2]);
		
		BufferedImage eImage = saveDataToBuff(fileName.getName(), encryptedData);
		
		try {
            ImageIO.write(eImage, "BMP", new File("encryptedFile.bmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		// Write BufferedImage to BMP file
        BMPImage encryptedImage = new BMPImage("encryptedFile.bmp");
		
		// Display encrypted image
		SwingUtilities.invokeLater(() -> createAndShowGUI("Encrypted BMP Image", encryptedImage.getName()));
		
		// Decrypting Encrypted Data
		BufferedImage dImage = seeBMPImage(encryptedImage.getName(), encryptedData, publicKeyPrivateKey[1], publicKeyPrivateKey[2]);
		
		try {
            ImageIO.write(dImage, "BMP", new File("decryptedFile.bmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		// Write BufferedImage to BMP file
        BMPImage decryptedImage = new BMPImage("decryptedFile.bmp");
		
		// Display decrypted image
		SwingUtilities.invokeLater(() -> createAndShowGUI("Decrypted BMP Image", decryptedImage.getName()));
		
	}
	
	
	
	private static class RGBColor {
		// stored color values
		private BigInteger colorRed;
		private BigInteger colorBlue;
		private BigInteger colorGreen;
		// encrypted values
		private BigInteger encryptedRed;
		private BigInteger encryptedBlue;
		private BigInteger encryptedGreen;
		// stored hex value
		private int color;
		
		public RGBColor(BigInteger red, BigInteger green, BigInteger blue) {
			setTrueRed(red);
			setTrueGreen(green);
			setTrueBlue(blue);
			setColor();
		}
		
		public void setTrueRed(BigInteger red) {
			encryptedRed = red;
			
			if(red.compareTo(MAX)==1) {
				colorRed = red.mod(MAX);
			}
			else
				colorRed = encryptedRed;
		}
		
		public void setTrueBlue(BigInteger blue) {
			encryptedBlue = blue;
			
			if(blue.compareTo(MAX)==1) {
				colorBlue = blue.mod(MAX);
			}
			else
				colorBlue = encryptedBlue;
		}
		
		public void setTrueGreen(BigInteger green) {
			encryptedGreen = green;
			
			if(green.compareTo(MAX)==1) {
				colorGreen = green.mod(MAX);
			}
			else
				colorGreen = encryptedGreen;
		}
		
		public void setColor() {
			color = ((colorRed.intValue() & 0xff) << 16) | ((colorGreen.intValue() & 0xff) << 8) | (colorBlue.intValue() & 0xff);
		}
		
		public BigInteger getEncryptedRed() {
			return encryptedRed;
		}
		
		public BigInteger getEncryptedBlue() {
			return encryptedBlue;
		}
		
		public BigInteger getEncryptedGreen() {
			return encryptedGreen;
		}
	}
	
	private static BigInteger[] generateRSAKeys(int keySize) {
		BigInteger p = BigInteger.probablePrime(keySize/2, random);
        BigInteger q = BigInteger.probablePrime(keySize/2, random);
		
		//Calculate public key, n
		BigInteger n = p.multiply(q);
		
		// Calculate phi of n, which is secret information.
		BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));
		
		// Calculate e such that it is coprime with phi of n.
		BigInteger e = null;
		boolean done = false;
		while (!done) {
			e = new BigInteger(phi.bitCount(), random);
			if (phi.compareTo(e)==1 &&(e.gcd(phi)).equals(one) && e.compareTo(one)==1)
				done = true;
		}
		
		// Calculate the secret key d. This can be done only with
		// knowledge of phi.
		BigInteger d = e.modInverse(phi);
		return new BigInteger[]{e, d, n}; // Public and private keys
	}
	
	public static BigInteger convert (int x) {
		String x_String = Integer.toString(x);
		BigInteger BInt = new BigInteger(x_String);
		return BInt;
	}
	
	public static BigInteger colorEncrypt (int m, BigInteger key, BigInteger n) {
		BigInteger newcolor = convert(m);
		newcolor = newcolor.modPow(key, n);
		return newcolor;
	}
	
	public static RGBColor[][] seeBMPImageData(String BMPFileName, BigInteger key, BigInteger n) throws IOException {
	    BufferedImage image = ImageIO.read(new File(BMPFileName));
	   
	    RGBColor [][] RGBArray = new RGBColor [image.getWidth()][image.getHeight()];

	    for (int xPixel = 0; xPixel < image.getWidth(); xPixel++)
	    {
	        for (int yPixel = 0; yPixel < image.getHeight(); yPixel++)
	        {
	            int color = image.getRGB(xPixel, yPixel);

		        // Components will be in the range of 0..255:	            
	            int red = (color & 0xff0000) >> 16;
		        int green = (color & 0xff00) >> 8;
		        int blue = color & 0xff;
		        
	            RGBColor newColor = new RGBColor(colorEncrypt(red, key, n), colorEncrypt(green, key, n), colorEncrypt(blue, key, n));
	            
	            RGBArray[xPixel][yPixel] = newColor;
	        }
	    }
	    
	    return RGBArray;
	}
	
	public static BufferedImage saveDataToBuff(String BMPFileName, RGBColor [][] data) throws IOException {
		BufferedImage image = ImageIO.read(new File(BMPFileName));

	    for (int xPixel = 0; xPixel < image.getWidth(); xPixel++)
	    {
	        for (int yPixel = 0; yPixel < image.getHeight(); yPixel++)
	        {
	        	image.setRGB(xPixel, yPixel, data[xPixel][yPixel].color);
	        }
	    }
	    return image;
	}
	
	public static BufferedImage seeBMPImage(String BMPFileName, RGBColor[][] Data, BigInteger key, BigInteger n) throws IOException {
	    BufferedImage image = ImageIO.read(new File(BMPFileName));

	    for (int xPixel = 0; xPixel < image.getWidth(); xPixel++)
	    {
	        for (int yPixel = 0; yPixel < image.getHeight(); yPixel++)
	        {

		        // Components will not be in the range of 0..255:
	            int red = Data[xPixel][yPixel].getEncryptedRed().intValue();
		        int green = Data[xPixel][yPixel].getEncryptedGreen().intValue();
		        int blue = Data[xPixel][yPixel].getEncryptedBlue().intValue();
	            
	            // These components will be in the range of 0...255:
	            int decryptedRed = colorEncrypt(red, key, n).intValue();
	            int decryptedGreen = colorEncrypt(green, key, n).intValue();
	            int decryptedBlue = colorEncrypt(blue, key, n).intValue();
	            
	            
//	            int newColor = new Color(0, 0, 255).getRGB();
	            int newColor = ((decryptedRed & 0xff) << 16) | ((decryptedGreen & 0xff) << 8) | (decryptedBlue & 0xff);
	            image.setRGB(xPixel, yPixel, newColor);
	        }
	    }
	    
	    return image;
	}
	
	private static class BMPImage {
        private String name;
		private int width;
        private int height;
        private byte[] pixelData;

        public BMPImage(String fileName) throws IOException {
            name = fileName;
        	FileInputStream fis = new FileInputStream(fileName);
            
            // Read BMP header and extract image dimensions
            fis.skip(18);
            byte[] widthBytes = new byte[4];
            fis.read(widthBytes);
            width = ByteBuffer.wrap(widthBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();

            byte[] heightBytes = new byte[4];
            fis.read(heightBytes);
            height = ByteBuffer.wrap(heightBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();

            // Skip to pixel data
            fis.skip(28);

            // Read pixel data
            pixelData = new byte[width * height * 3];
            fis.read(pixelData);

            fis.close();
        }

        public String getName() {
        	return name;
        }
        
        public int getHeight() {
        	return height;
        }
        
        public int getWidth() {
        	return width;
        }
    }
	
	private static void createAndShowGUI(String message, String fileName) {
        JFrame frame = new JFrame(message);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        JLabel label = new JLabel();
        try {
            BufferedImage image = ImageIO.read(new File(fileName));
            label.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            e.printStackTrace();
            label.setText("Error loading image");
        }

        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
