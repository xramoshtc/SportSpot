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
    public partial class UCReserves : UserControl
    {
        public UCReserves()
        {
            InitializeComponent();
        }

        private void UCReserves_Load(object sender, EventArgs e)
        {
            LoadReserves();
        }


        private async void LoadReserves()
        {
            var service = new BookingService();
            var reserves = await service.GetMyBookings();
            dataGridViewReserves.DataSource = reserves;

            dataGridViewReserves.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dataGridViewReserves.ReadOnly = true;
            dataGridViewReserves.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dataGridViewReserves.MultiSelect = false;

            dataGridViewReserves.Columns["courtId"].HeaderText = "Pista";
            dataGridViewReserves.Columns["dateTime"].HeaderText = "Data i hora";
            dataGridViewReserves.Columns["durationMinutes"].HeaderText = "Durada (min)";

            dataGridViewReserves.DataSource = reserves;
            AfegirColumnaCancelar();
        }

        private async void dataGridViewReserves_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex < 0)
                return;

            // Comprovar si s'ha clicat la columna Cancelar
            if (dataGridViewReserves.Columns[e.ColumnIndex].Name == "Cancelar")
            {
                try
                {
                    // Obtenir l'ID de la reserva
                    long bookingId = (long)dataGridViewReserves.Rows[e.RowIndex].Cells["id"].Value;

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
            if (dataGridViewReserves.Columns.Contains("Cancelar"))
                return;

            var btn = new DataGridViewButtonColumn();
            btn.Name = "Cancelar";
            btn.HeaderText = "Acció";
            btn.Text = "Cancel·lar";
            btn.UseColumnTextForButtonValue = true;

            dataGridViewReserves.Columns.Add(btn);
        }

    }
}
