using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Models
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe que representa un usuari de l'aplicació, amb propietats com id, nom, contrasenya, email, rol i estat actiu. 
    /// Aquesta classe es pot utilitzar per gestionar la informació dels usuaris i les seves credencials dins de l'aplicació.
    /// </summary>
    public class Usuari
    {
        public int id { get; set; }
        public string name { get; set; }
        public string password { get; set; }
        public string email { get; set; }
        public string role { get; set; }
        public bool active { get; set; }
    }
}
