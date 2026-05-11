using SportSpot.Services;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace SportSpot.Controls
{
    public partial class UCReservesAdmin : UserControl
    {
        public UCReservesAdmin()
        {
            InitializeComponent();
        }

        private async void UCReservesAdmin_Load(object sender, EventArgs e)
        {
            var service = new BookingService();
            var reserves = await service.GetBookingsByCourt(0);
            dataGridViewReservesAdmin.DataSource = reserves;

            dataGridViewReservesAdmin.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dataGridViewReservesAdmin.ReadOnly = true;
            dataGridViewReservesAdmin.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dataGridViewReservesAdmin.MultiSelect = false;

            await CarregarPistes();
        }

        private async Task CarregarPistes()
        {
            var service = new PistaService(); 
            var pistes = await service.GetPistesAsync();
                        
            comboBoxPistes.DisplayMember = "name";   
            comboBoxPistes.ValueMember = "id";
            comboBoxPistes.DataSource = pistes;
        }

        private async void comboBoxPistes_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBoxPistes.SelectedValue == null)
                return;

            int courtId = (int)comboBoxPistes.SelectedValue;

            var service = new BookingService();
            var reserves = await service.GetBookingsByCourt(courtId);

            dataGridViewReservesAdmin.DataSource = reserves;
        }



        private async void LoadReserves()
        {
            var service = new BookingService();
            var reserves = await service.GetMyBookings();
            dataGridViewReservesAdmin.DataSource = reserves;

            dataGridViewReservesAdmin.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dataGridViewReservesAdmin.ReadOnly = true;
            dataGridViewReservesAdmin.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dataGridViewReservesAdmin.MultiSelect = false;

            dataGridViewReservesAdmin.Columns["courtId"].HeaderText = "Pista";
            dataGridViewReservesAdmin.Columns["dateTime"].HeaderText = "Data i hora";
            dataGridViewReservesAdmin.Columns["durationMinutes"].HeaderText = "Durada (min)";

            dataGridViewReservesAdmin.DataSource = reserves;
            AfegirColumnaCancelar();
        }

        private async void dataGridViewReserves_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex < 0)
                return;

            // Comprovar si s'ha clicat la columna Cancelar
            if (dataGridViewReservesAdmin.Columns[e.ColumnIndex].Name == "Cancelar")
            {
                try
                {
                    // Obtenir l'ID de la reserva
                    long bookingId = (long)dataGridViewReservesAdmin.Rows[e.RowIndex].Cells["id"].Value;

                    var service = new BookingService();
                    bool ok = await service.DeleteBooking(bookingId);

                    if (ok)
                    {
                        MessageBox.Show("Reserva cancel·lada correctament.");
                        LoadReserves(); // refrescar llistat
                    }
                    else
                    {
                        MessageBox.Show("No s'ha pogut cancel·lar la reserva.");
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Error en cancel·lar la reserva: " + ex.Message);
                }
            }
        }

        private void AfegirColumnaCancelar()
        {
            if (dataGridViewReservesAdmin.Columns.Contains("Cancelar"))
                return;

            var btn = new DataGridViewButtonColumn();
            btn.Name = "Cancelar";
            btn.HeaderText = "Acció";
            btn.Text = "Cancel·lar";
            btn.UseColumnTextForButtonValue = true;

            dataGridViewReservesAdmin.Columns.Add(btn);
        }

    }
}
