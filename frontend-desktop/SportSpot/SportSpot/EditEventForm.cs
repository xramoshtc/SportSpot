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
    /// Classe que representa el formulari per editar un esdeveniment existent. Permet modificar el títol, la pista i la data/hora de l'esdeveniment.
    /// </summary>
    public partial class EditEventForm : Form
    {
        private readonly int _eventId;
        private readonly string _title;
        private readonly string _courtName;
        private readonly DateTime _dateTime;

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode constructor que inicialitza el formulari amb les dades de l'esdeveniment a editar. Rep com a paràmetres l'identificador de l'esdeveniment, el títol, el nom de la pista i la data/hora actuals de l'esdeveniment. Carrega les pistes disponibles i selecciona la pista corresponent al nom rebut.
        /// </summary>
        /// <param name="eventId">ID de l'esdeveniment a editar.</param>
        /// <param name="title">Títol de l'esdeveniment.</param>
        /// <param name="courtName">Nom de la pista associada a l'esdeveniment.</param>
        /// <param name="dateTime">Data i hora de l'esdeveniment.</param>
        public EditEventForm(int eventId, string title, string courtName, DateTime dateTime)
        {
            InitializeComponent();

            _eventId = eventId;
            _title = title;
            _courtName = courtName;
            _dateTime = dateTime;

            this.Load += EditEventForm_Load;
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que s'executa quan el formulari es carrega. Carrega les pistes disponibles i inicialitza els controls del formulari amb les dades de l'esdeveniment a editar. Selecciona la pista corresponent al nom rebut en el constructor.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private async void EditEventForm_Load(object sender, EventArgs e)
        {
            await LoadPistes();

            txtTitle.Text = _title;
            dtpDateTime.Value = _dateTime;
            // Seleccionar per nom
            cmbPistes.SelectedIndex = cmbPistes.FindStringExact(_courtName);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que carrega les pistes disponibles des del servei de pistes i les assigna al control ComboBox del formulari. Estableix el nom de la pista com a element a mostrar i l'identificador de la pista com a valor associat a cada element del ComboBox.
        /// </summary>
        /// <returns>Tasca que representa l'operació asíncrona.</returns>
        private async Task LoadPistes()
        {
            var pistaService = new PistaService();
            var pistes = await pistaService.GetPistesAsync();

            cmbPistes.DataSource = pistes;
            cmbPistes.DisplayMember = "name";
            cmbPistes.ValueMember = "id";
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que s'executa quan l'usuari fa clic al botó de modificar l'esdeveniment. Recull les dades modificades del formulari, crea un objecte anònim amb els camps actualitzats i crida al servei d'esdeveniments per actualitzar l'esdeveniment amb les noves dades. Mostra un missatge de confirmació i tanca el formulari després de l'actualització.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private async void btnEditEvent_Click(object sender, EventArgs e)
        {
            var update = new
            {
                title = txtTitle.Text,
                courtId = (int)cmbPistes.SelectedValue,
                dateTime = dtpDateTime.Value
            };

            var service = new EventService();
            await service.UpdateEvent(_eventId, update);

            MessageBox.Show("Esdeveniment modificat correctament.");
            this.Close();
        }
    }
}

