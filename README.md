# Secure msg
This mod can encrypt messages between 2 clients so that servers can not read them.  


## Usage
You and the player you want to message need to have this mod installed and share the same key.  
To send a player a secure message they need to have an entry in your configuration.
If they have an entry you can use the /smsg (name) (message) command. If you dont have a player in your entrylist you will get an error message that the player has not been found.


### Adding player entries
Go to the key menu by clicking the new K button in the Options screen.  
In this screen you can see 2 input fields with labels and an add button next to it.  
You enter the name of the player you want to have a secure message link with in the Player text box.  
If you do not have a secret key yet click generate key at the bottom of the screen. This will fill in the key text box and then you can click add.  
This should add an entry for that player. The key can be copied from the field and should only be shared with that player.

So as an example with players Bob and John.  
Bob makes a new entry with John's name and generates a new key.  
And John makes a new entry with Bob's name and the same key Bob gave him.

The key needs to be the same between the 2 players!

### Editing entries
To delete an entry you can click the X on the right side of that entry.
To edit a key you can edit the textbox of the entry and click the S on the left to apply it (not saved to the config).

### Saving
The save button at the bottom of the page saves the entries to a file in your fabric configurations folder. This is used to keep the entries on restart. If you do not save your entries, they reset to the last save.

### Reloading
Reload config reloads the save file. This is helpful if you want to return to your last save, or if you manually add entries in the secure_msg.txt file. 

