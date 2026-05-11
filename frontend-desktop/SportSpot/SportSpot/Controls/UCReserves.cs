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
    /// <summary>
    /// Autor: Miquel Uribe Faixedas
    /// Classe que mostra les reserves del usuari i permet cancel·lar-les si encara no han passat. Utilitza el servei BookingService per obtenir les reserves i gestionar la cancel·lació.
    /// </summary>
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

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que carrega les reserves de l'usuari mitjançant el BookingService i les mostra en un DataGridView. Configura les columnes i afegeix un botó per cancel·lar cada reserva.
        /// </summary>
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

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que gestiona el clic en el botó de cancel·lar reserva. Comprova si s'ha clicat la columna correcta, obté l'ID de la reserva i crida al BookingService per cancel·lar-la. Si la cancel·lació és correcta, refresca el llistat de reserves.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
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

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que afegeix una columna de botó al DataGridView per permetre la cancel·lació de les reserves. Comprova si la columna ja existeix abans d'afegir-la per evitar duplicats.
        /// </summary>
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
