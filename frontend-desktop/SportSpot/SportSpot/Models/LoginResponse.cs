using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Models
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe per llegir la resposta del backend després de fer login. 
    /// Conté informació sobre si el login ha estat correcte, un missatge, un codi de resultat, 
    /// un token de sessió i el rol de l'usuari.
    /// </summary>
    public class LoginResponse
    {
        public bool success { get; set; }
        public string message { get; set; }
        public int resultCode { get; set; }
        public string sessionToken { get; set; }
        public string role { get; set; }
    }
}
