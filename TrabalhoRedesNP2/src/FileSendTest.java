import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import javax.swing.JFileChooser;

public class FileSendTest {
	private String fileName;
	public static void main(String[] args) {
		FileSendTest test = new FileSendTest();
		JFileChooser jfc = new JFileChooser();
		jfc.setAcceptAllFileFilterUsed(true);
		jfc.showOpenDialog(null);
		Socket socket = null;
		FileInputStream in = null;
		CheckedOutputStream cout = null;
		try {
			File file = jfc.getSelectedFile();
			if (file.canRead()) {
				test.setFILE_NAME(file.getName());
				Server s = new Server(test);
				s.start();
				socket = new Socket("127.0.0.1",Server.PORT);
				cout = new CheckedOutputStream(socket.getOutputStream(), new CRC32());
				in = new FileInputStream(file);
				byte[] buffer = new byte[Server.BUFFER_SIZE];
				System.out.println(Calendar.getInstance().getTimeInMillis());
				while (( in.read(buffer) )!= -1) {
					cout.write(buffer);
				}
				cout.close();
				in.close();
				System.out.println(Calendar.getInstance().getTimeInMillis());
				System.out.println("Client CRC: "+Long.toHexString(cout.getChecksum().getValue()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			System.out.println("Clien Out...");
			try{
				if(cout != null){
					cout.close();
				}
				if(socket != null){
					socket.close();
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
	public String getFilename() {
		return this.fileName;
	}
	public void setFILE_NAME(String fileName) {
		this.fileName = fileName;
	}
}
