using SportSpot.Models;
using SportSpot.Services;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Text.Json;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace SportSpot
{
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe que representa el formulari per crear un nou esdeveniment. Permet als usuaris introduir el títol, seleccionar una pista i triar la data i hora de l'esdeveniment. Utilitza els serveis de PistaService per carregar les pistes disponibles i EventService per crear l'esdeveniment al backend.
    /// </summary>
    public partial class NewEventForm : Form
    {
        private readonly PistaService _pistaService;

        public NewEventForm()
        {
            InitializeComponent();
            _pistaService = new PistaService();
            this.Load += NewEventForm_Load;
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que s'executa quan el formulari es carrega. Carrega les pistes disponibles al combo box i estableix la data i hora per defecte a una hora més que l'actual. Això facilita als usuaris la creació d'esdeveniments futurs sense haver de configurar manualment la data i hora cada vegada.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private async void NewEventForm_Load(object sender, EventArgs e)
        {
            await LoadPistes();
            dtpDateTime.Value = DateTime.Now.AddHours(1); // opcional
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que carrega les pistes disponibles des del backend utilitzant el servei PistaService. Un cop obtingudes les pistes, les assigna com a font de dades al combo box, permetent als usuaris seleccionar la pista on es realitzarà l'esdeveniment. Aquest mètode és crucial per assegurar que els usuaris tinguin accés a la informació actualitzada de les pistes disponibles abans de crear un esdeveniment.
        /// </summary>
        /// <returns>Tasca que representa l'operació asíncrona.</returns>
        private async Task LoadPistes()
        {
            var pistes = await _pistaService.GetPistesAsync();
            // pistes ha de ser List<PistaItem> o similar

            cmbPistes.DataSource = pistes;
            cmbPistes.DisplayMember = "Name";
            cmbPistes.ValueMember = "Id";
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que s'executa quan l'usuari fa clic al botó de crear esdeveniment. Valida que el títol no estigui buit i que s'hagi seleccionat una pista. Si les validacions són correctes, crea un objecte CreateEventRequest amb les dades introduïdes per l'usuari i crida el servei EventService per crear l'esdeveniment al backend. Un cop creat l'esdeveniment, mostra un missatge de confirmació i tanca el formulari retornant un resultat OK.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private async void btnCreateEvent_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtTitle.Text))
            {
                MessageBox.Show("El títol és obligatori.");
                return;
            }

            if (cmbPistes.SelectedValue == null)
            {
                MessageBox.Show("Has de seleccionar una pista.");
                return;
            }

            int courtId = (int)cmbPistes.SelectedValue;

            var request = new CreateEventRequest
            {
                title = txtTitle.Text.Trim(),
                courtId = courtId,
                dateTime = dtpDateTime.Value
            };

            var service = new EventService();
            await service.CreateEvent(request);

            MessageBox.Show("Esdeveniment creat correctament!");
            this.DialogResult = DialogResult.OK;
            this.Close();
        }
    }
}

