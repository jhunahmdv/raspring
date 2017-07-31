
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

import sun.nio.ByteBuffered;

import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;
import com.pi4j.util.Console;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;

public class SerialThread extends Thread{

	public static String[] prtcl=	{/*"50301801004E9B03FA", */"50300C01000E9F03FA", "50300D01019E9F03FA"};
	public static String[] str  = 	{/*"keypad status",      */"Filling status", "Filling info"};
	public final static String ACK = "50C0FA";
	public final static String POLL = "5020FA";
	
//	public static HashMap<Integer, String[]> dict = new HashMap<Integer, String[]>();

	private static ByteBuffer buffer;
	static Serial serial;
//	private static Console console;
	static String hex_OutData = "";
	static String hex_InData = "";
	static boolean flag = false;	
	
	
	public SerialThread() throws Exception{
		initialize();

//		comm = new SerialCommunication();
//		comm.start();
	}
	
	private void initialize() throws Exception{
		buffer = ByteBuffer.allocate(128);
		
//		console = new Console();

        // print program title/header
//        console.title("<-- The Pi4J Project -->", "Serial Communication Example");

        // allow for user to exit program using CTRL-C
//        console.promptForExit();

        // create an instance of the serial communications class
        serial = SerialFactory.createInstance();
        
        try {
            // create serial config object
            SerialConfig config = new SerialConfig();

            // set default serial settings (device, baud rate, flow control, etc)
            //
            // by default, use the DEFAULT com port on the Raspberry Pi (exposed on GPIO header)
            // NOTE: this utility method will determine the default serial port for the
            //       detected platform and board/model.  For all Raspberry Pi models
            //       except the 3B, it will return "/dev/ttyAMA0".  For Raspberry Pi
            //       model 3B may return "/dev/ttyS0" or "/dev/ttyAMA0" depending on
            //       environment configuration.
            config.device("/dev/ttyUSB0") 
                  .baud(Baud._9600)
                  .dataBits(DataBits._8)
                  .parity(Parity.ODD)
                  .stopBits(StopBits._1)
                  .flowControl(FlowControl.NONE);
            

            // display connection details
//            console.box(" Connecting to: " + config.toString(),
//                    " We are sending ASCII data on the serial port every 1 second.",
//                    " Data received on serial port will be displayed below.");


            // open the default serial device/port with the configuration settings
            serial.open(config);
            	
//            Thread.sleep(1000);
        }catch(IOException ex) {
//            console.println(" ==>> SERIAL SETUP FAILED : " + ex.getMessage());
        	ex.printStackTrace();
            return;
        } 
        
	}
	
	public static int showRandomInteger(int aStart, int aEnd, Random aRandom){
	    if (aStart > aEnd) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    //get the range, casting to long to avoid overflow problems
	    long range = (long)aEnd - (long)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    int randomNumber =  (int)(fraction + aStart);    
	    return randomNumber;
	  }
	
	public static void sendMessage(String outMsg) throws Exception{
		if (hex_InData.startsWith(ACK)){
			write(POLL); 
		}else if (!hex_InData.equals("") && !hex_InData.startsWith(ACK) && !hex_InData.startsWith("5070FA") && flag == true){
			write(ACK);
			hex_InData = "";
		}else{
			write(outMsg); 
		} 
	}
	
	public static void getMessage() throws Exception{
		
		// Serialdan data oxu
		if (hex_OutData.equals(ACK)){ 
			return;
		}
		
		
		hex_InData = DatatypeConverter.printHexBinary(serial.read());
		System.out.println("RX	" + hex_InData); 
			
		if (!hex_InData.equals(ACK))
			flag = true; 
	}

	private static void write(String outStr) throws Exception{
		hex_OutData = outStr; 
    	serial.write(DatatypeConverter.parseHexBinary(outStr));
		System.out.println("TX	" + hex_OutData);
	}
	
	private static ByteBuffer read() throws IllegalStateException, IOException{
		buffer.clear();
		serial.read(buffer);
		return buffer;
	}
	

}
