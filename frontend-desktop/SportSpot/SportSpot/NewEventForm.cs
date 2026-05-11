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
    public partial class NewEventForm : Form
    {
        private readonly PistaService _pistaService;

        public NewEventForm()
        {
            InitializeComponent();
            _pistaService = new PistaService();
            this.Load += NewEventForm_Load;
        }

        private async void NewEventForm_Load(object sender, EventArgs e)
        {
            await LoadPistes();
            dtpDateTime.Value = DateTime.Now.AddHours(1); // opcional
        }

        private async Task LoadPistes()
        {
            var pistes = await _pistaService.GetPistesAsync();
            // pistes ha de ser List<PistaItem> o similar

            cmbPistes.DataSource = pistes;
            cmbPistes.DisplayMember = "Name";
            cmbPistes.ValueMember = "Id";
        }


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

