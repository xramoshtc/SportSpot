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
    public partial class EditEventForm : Form
    {
        private readonly int _eventId;
        private readonly string _title;
        private readonly string _courtName;
        private readonly DateTime _dateTime;

        public EditEventForm(int eventId, string title, string courtName, DateTime dateTime)
        {
            InitializeComponent();

            _eventId = eventId;
            _title = title;
            _courtName = courtName;
            _dateTime = dateTime;

            this.Load += EditEventForm_Load;
        }


        private async void EditEventForm_Load(object sender, EventArgs e)
        {
            await LoadPistes();

            txtTitle.Text = _title;
            dtpDateTime.Value = _dateTime;
            // Seleccionar per nom
            cmbPistes.SelectedIndex = cmbPistes.FindStringExact(_courtName);
        }

        private async Task LoadPistes()
        {
            var pistaService = new PistaService();
            var pistes = await pistaService.GetPistesAsync();

            cmbPistes.DataSource = pistes;
            cmbPistes.DisplayMember = "name";
            cmbPistes.ValueMember = "id";
        }


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

