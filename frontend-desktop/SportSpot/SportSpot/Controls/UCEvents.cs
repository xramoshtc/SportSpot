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
    public partial class UCEvents : UserControl
    {


        public UCEvents()
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
            AfegirColumnesAccions();
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
        /// Mètode per afegir les columnes d'accions (Modificar i Eliminar) al DataGridView. Aquest mètode crea
        /// dues columnes de botons, una per modificar i una altra per eliminar, i les afegeix al DataGridView. 
        /// Cada botó té un text representat per un emoji (✏️ per modificar i 🗑️ per eliminar) i està configurat 
        /// per utilitzar aquest text com a valor del botó. Aquestes columnes permeten als usuaris interactuar 
        /// amb cada esdeveniment de manera fàcil i visual, facilitant la gestió dels esdeveniments directament 
        /// des del DataGridView.
        /// </summary>
        private void AfegirColumnesAccions()
        {
            // Evitar duplicats si recarregues
            if (dataGridViewEvents.Columns.Contains("Modificar")) return;

            // Botó Modificar
            var btnModificar = new DataGridViewButtonColumn();
            btnModificar.HeaderText = "Modificar";
            btnModificar.Text = "✏️";
            btnModificar.Name = "Modificar";
            btnModificar.UseColumnTextForButtonValue = true;
            dataGridViewEvents.Columns.Add(btnModificar);

            // Botó Eliminar
            var btnEliminar = new DataGridViewButtonColumn();
            btnEliminar.HeaderText = "Eliminar";
            btnEliminar.Text = "🗑️";
            btnEliminar.Name = "Eliminar";
            btnEliminar.UseColumnTextForButtonValue = true;
            dataGridViewEvents.Columns.Add(btnEliminar);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que es crida quan es fa clic a una cel·la del DataGridView. Aquest mètode comprova si el clic es va 
        /// fer en una fila vàlida i determina si el botó clicat és el de modificar o eliminar. Si es tracta del botó de 
        /// modificar, crida al mètode EditEvent amb l'ID de l'esdeveniment corresponent. Si es tracta del botó d'eliminar,
        /// crida al mètode DeleteEvent amb l'ID de l'esdeveniment corresponent. Aquest mètode permet als usuaris 
        /// gestionar els esdeveniments directament des del DataGridView, facilitant la modificació i eliminació 
        /// d'esdeveniments de manera ràpida i intuïtiva.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private async void dataGridViewEvents_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex < 0) return;

            var id = (int)dataGridViewEvents.Rows[e.RowIndex].Cells["id"].Value;

            if (dataGridViewEvents.Columns[e.ColumnIndex].Name == "Modificar")
            {
                EditEvent(e.RowIndex);
            }
            else if (dataGridViewEvents.Columns[e.ColumnIndex].Name == "Eliminar")
            {
                DeleteEvent(id);
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per eliminar un esdeveniment. Aquest mètode mostra una finestra de confirmació per assegurar-se que 
        /// l'usuari vol eliminar l'esdeveniment. Si l'usuari confirma, el mètode crida al servei per eliminar 
        /// l'esdeveniment amb l'ID proporcionat. Després d'eliminar l'esdeveniment, mostra un missatge de confirmació 
        /// i recarrega la llista d'esdeveniments per reflectir els canvis.
        /// </summary>
        /// <param name="id"></param>
        private async void DeleteEvent(int id)
        {
            var confirm = MessageBox.Show("Segur que vols eliminar l'esdeveniment?",
                                          "Confirmació",
                                          MessageBoxButtons.YesNo);

            if (confirm == DialogResult.No) return;

            var service = new EventService();
            await service.DeleteEvent(id);

            MessageBox.Show("Esdeveniment eliminat.");
            LoadEvents();
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per editar un esdeveniment. Aquest mètode crea una instància del formulari EditEventForm, passant 
        /// l'ID de l'esdeveniment que es vol editar. El formulari EditEventForm permet a l'usuari modificar 
        /// els detalls de l'esdeveniment.
        /// </summary>
        /// <param name="id">id de l'esdeveniment que es vol editar.</param>
        private void EditEvent(int rowIndex)
        {
            var row = dataGridViewEvents.Rows[rowIndex];
               
            int id = (int)row.Cells["id"].Value;
            string title = row.Cells["title"].Value.ToString();
            string courtName = row.Cells["courtName"].Value.ToString();
            DateTime dateTime = (DateTime)row.Cells["dateTime"].Value;

            var frm = new EditEventForm(id, title, courtName, dateTime);
            frm.ShowDialog();

            LoadEvents();
        }


        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        ///
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private void btnNouEvent_Click(object sender, EventArgs e)
        {
            var frm = new NewEventForm();
            if (frm.ShowDialog() == DialogResult.OK)
            {
                LoadEvents();
            }
        }

    }
}
