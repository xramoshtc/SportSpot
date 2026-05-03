using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Models
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe que representa una pista esportiva. Conté propietats com id, nom, tipus, ubicació, preu 
    /// per hora i capacitat. Aquesta classe s'utilitza per deserialitzar les dades de les pistes obtingudes
    /// de l'API i per mostrar-les a la interfície d'usuari. També es pot utilitzar per enviar dades de pistes 
    /// a l'API quan es creen o actualitzen pistes. 
    /// </summary>
    public class Pista
    {
        public int id { get; set; }
        public string name { get; set; }
        public string type { get; set; }
        public string location { get; set; }
        public decimal pricePerHour { get; set; }
        public int capacity { get; set; }
    }
}
