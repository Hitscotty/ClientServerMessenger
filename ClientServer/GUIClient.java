package ClientServer;
//----------------------------------------------------------------------------//

import java.io.IOException;
import java.net.Socket;

//----------------------------------------------------------------------------//


public class GUIClient extends GUIChat
{

   //-------------------------------------------------------------------------//

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


public GUIClient()
   {
      super();
      
   }
   

  //-------------------------------------------------------------------------//

  public Socket connect() throws IOException
  {
	Socket  socket = new Socket("localhost", 8080);
     return socket;
  }

  //-------------------------------------------------------------------------//

}