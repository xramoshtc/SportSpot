using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Models
{
    public static class Session
    {
        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Classe estàtica que emmagatzema la informació de la sessió de l'usuari, com el token,
        /// el rol i el nom d'usuari. Aquesta informació es pot utilitzar a tota l'aplicació 
        /// per verificar permisos i mantenir l'estat de la sessió.
        /// </summary>
        public static string sessionToken { get; set; }
        public static string role { get; set; }
        public static string user { get; set; }
    }
}
