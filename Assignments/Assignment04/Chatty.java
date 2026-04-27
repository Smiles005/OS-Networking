package OS&Networking.Assignments.Assignment04;

public class Chatty {
    
}
//For this assignment, we will improve the design of our 'chat' system.

// You will need to write two separate programs: a client and a server.

// Messages on this system will contain two parts: A user name and a message.  Send the parts separately (i.e. send the user name then send the message for that user).

 

// The Server will need to do the following:

// Listen on port 4096
// Handle multiple connections
// Store all the connected sockets mapped to user names.  Ensure all threads have access to this map.  When a connection is closed, remove the data from the map.
// Whenever a message comes in, lookup the socket for the specified user, and deliver the message to that person.
// Message delivery must contain two parts: the name of the person who sent the message, and the message itself.
 

// The Client will need to do the following:

// Prompt for a user name
// Connect to the server and send the user name
// Print out any messages received along with the name of the person who sent the message
// Allow the user to send messages to the server by first typing in a user name, and then a message.
// Please note: Everyone's code should work with everyone else's code. I will write a server and a client to test your code.