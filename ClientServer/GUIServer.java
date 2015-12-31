package ClientServer;
//----------------------------------------------------------------------------//

import java.io.*;
import java.net.*;

//----------------------------------------------------------------------------//


public class GUIServer extends GUIChat
{

   //-------------------------------------------------------------------------//

  
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
ServerSocket socket;
public GUIServer()
   {
      super();
   }

  //-------------------------------------------------------------------------//

  public Socket connect() throws IOException
  {
	  socket = new ServerSocket(8080);
	     return socket.accept();
   }

   //-------------------------------------------------------------------------//


}
