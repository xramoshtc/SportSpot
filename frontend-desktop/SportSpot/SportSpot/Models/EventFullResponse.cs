using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Models
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe per quan fem UpdateEvent, on volem retornar tota la informació de l'esdeveniment actualitzada,
    /// incloent els participants i l'organitzador. Aquesta classe és similar a EventResponse però amb més 
    /// detalls, com els objectes complets de l'organitzador i els participants en lloc de només els seus
    /// noms. Això permet als clients obtenir una visió completa de l'esdeveniment després d'una
    /// actualització.
    /// </summary>
    public class EventFullResponse
    {
        public int id { get; set; }
        public string title { get; set; }
        public DateTime dateTime { get; set; }
        public Usuari organizer { get; set; }
        public Pista court { get; set; }
        public List<Usuari> participants { get; set; }
    }
}
