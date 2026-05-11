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
    /// Classe que gestiona les reserves de les pistes per part de l'administrador. Permet veure totes les reserves, filtrar per pista i cancel·lar reserves.
    /// </summary>
    public partial class UCReservesAdmin : UserControl
    {
        public UCReservesAdmin()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per carregar les reserves de les pistes quan es carrega el control. També configura el DataGridView per mostrar les reserves i crida al mètode per carregar les pistes al ComboBox.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
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

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per carregar les pistes disponibles al ComboBox. Utilitza el servei PistaService per obtenir la llista de pistes i configurar el ComboBox amb els noms i IDs de les pistes.
        /// </summary>
        /// <returns>Tasca que representa l'operació asíncrona.</returns>
        private async Task CarregarPistes()
        {
            var service = new PistaService(); 
            var pistes = await service.GetPistesAsync();
                        
            comboBoxPistes.DisplayMember = "name";   
            comboBoxPistes.ValueMember = "id";
            comboBoxPistes.DataSource = pistes;
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que es crida quan l'usuari selecciona una pista al ComboBox. Obté l'ID de la pista seleccionada i utilitza el servei BookingService per obtenir les reserves associades a aquesta pista. Actualitza el DataGridView amb les reserves filtrades per la pista seleccionada.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private async void comboBoxPistes_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBoxPistes.SelectedValue == null)
                return;

            int courtId = (int)comboBoxPistes.SelectedValue;

            var service = new BookingService();
            var reserves = await service.GetBookingsByCourt(courtId);

            dataGridViewReservesAdmin.DataSource = reserves;
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per carregar les reserves al DataGridView. Utilitza el servei BookingService per obtenir la llista de reserves i configurar el DataGridView amb les dades obtingudes. També crida al mètode per afegir la columna de cancel·lació al DataGridView.
        /// </summary>
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

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que es crida quan l'usuari fa clic a una cel·la del DataGridView. Comprova si s'ha clicat la columna de cancel·lació i, en cas afirmatiu, obté l'ID de la reserva associada a la fila seleccionada i utilitza el servei BookingService per cancel·lar la reserva. Mostra un missatge de confirmació o error segons el resultat de l'operació i actualitza el llistat de reserves després de la cancel·lació.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
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

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per afegir una columna de botó al DataGridView que permet cancel·lar les reserves. Comprova si la columna ja existeix per evitar duplicats i, en cas contrari, crea una nova columna de botó amb el text "Cancel·lar" i l'afegeix al DataGridView.
        /// </summary>
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
