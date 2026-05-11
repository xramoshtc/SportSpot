using SportSpot.Models;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using SportSpot.Services;

namespace SportSpot.Controls
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// 
    /// </summary>
    public partial class UCEventsClient : UserControl
    {


        public UCEventsClient()
        {
            InitializeComponent();

        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per carregar els esdeveniments des del servei i mostrar-los al DataGridView. El mètode crea 
        /// una instància del EventService, crida al mètode GetEvents per obtenir la llista d'esdeveniments i 
        /// assigna aquesta llista com a font de dades del DataGridView. Finalment, crida al mètode AfegirColumnesAccions 
        /// per afegir les columnes de botons d'acció al DataGridView.
        /// </summary>
        private async void LoadEvents()
        {
            var service = new EventService();
            var events = await service.GetEvents();

            dataGridViewEvents.DataSource = events;
            AfegirColumnaApuntar();
        }


        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que es crida quan es carrega el control UCEvents. Aquest mètode és responsable de carregar 
        /// els esdeveniments des del servei i mostrar-los al DataGridView quan el control es mostra per 
        /// primera vegada. Això assegura que els usuaris vegin la llista d'esdeveniments actualitzada cada 
        /// vegada que accedeixen a aquesta secció de l'aplicació.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private async void UCEvent_Load(object sender, EventArgs e)
        {
            LoadEvents();
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per afegir la columna d'acció "Apuntar" al DataGridView. Aquest mètode comprova si la columna "Apuntar" ja 
        /// existeix al DataGridView. Si no existeix, crea una columna de botó amb el text "Apunta'm" i l'afegeix al DataGridView.
        /// Aquesta columna permet als usuaris apuntar-se als esdeveniments directament des del DataGridView, facilitant la gestió
        /// de la participació en esdeveniments de manera ràpida i intuïtiva.
        /// </summary>
        private void AfegirColumnaApuntar()
        {
            if (dataGridViewEvents.Columns.Contains("Apuntar")) return;

            var btnApuntar = new DataGridViewButtonColumn();
            btnApuntar.HeaderText = "Acció";
            btnApuntar.Text = "Apunta'm";
            btnApuntar.Name = "Apuntar";
            btnApuntar.UseColumnTextForButtonValue = true;

            dataGridViewEvents.Columns.Add(btnApuntar);
        }


        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que es crida quan es fa clic en una cel·la del DataGridView. Aquest mètode comprova si la cel·la clicada és de
        /// la columna "Apuntar". Si és així, obté l'ID de l'esdeveniment corresponent a la fila clicada i crida al servei per 
        /// apuntar-se a l'esdeveniment. Després d'intentar apuntar-se, mostra un missatge de confirmació o error segons el 
        /// resultat i recarrega la llista d'esdeveniments per reflectir els canvis.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private async void dataGridViewEvents_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex < 0)
                return;

            if (dataGridViewEvents.Columns[e.ColumnIndex].Name == "Apuntar")
            {
                try
                {
                    int eventId = (int)dataGridViewEvents.Rows[e.RowIndex].Cells["id"].Value;

                    var service = new EventService();
                    var eventResponse = await service.JoinEvent(eventId);

                    if (eventResponse != null)
                    {
                        MessageBox.Show($"T'has apuntat correctament a l'esdeveniment: {eventResponse.title}");
                        LoadEvents();
                    }
                    else
                    {
                        MessageBox.Show("T'has apuntat correctament!");
                        LoadEvents();
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Error en apuntar-se a l'esdeveniment: " + ex.Message);
                }
            }
        }
    }
}
