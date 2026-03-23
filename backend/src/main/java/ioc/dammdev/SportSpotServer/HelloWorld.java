/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloWorld.java: pàgina web de benvinguda per comprovar la connexió del servlet
 * @author Gess Montalbán
 */
@RestController
public class HelloWorld {
    
    @GetMapping("/")
    public String helloWorld(){
        return "BENVINGUT, DAMM DEVELOPER! JA no estem en falles"
                + "</br></br>"
                + "<img src=\"/file.jpg\">";
    }
    
    
}
