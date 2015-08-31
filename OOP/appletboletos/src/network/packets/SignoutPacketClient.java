/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package network.packets;

import java.io.Serializable;

/**
 *
 * @author Castor
 */

public class SignoutPacketClient extends Packet implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * @param origin
     * @param target
     */
    public SignoutPacketClient(String origin, String target) {
        super(origin, target);
       
    }

   
    
}
