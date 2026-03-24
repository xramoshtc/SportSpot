using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Models
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe per representar el resultat de la crida al servei de login. 
    /// Conté informació sobre si el login ha estat exitós, un missatge associat, un codi de resultat,
    /// un token de sessió i el rol de l'usuari.
    /// </summary>
    public class LoginResult
    {
        public bool success { get; set; }
        public string message { get; set; }
        public string resultCode { get; set; }
        public string sessionToken { get; set; }
        public string role { get; set; }
    }
}
