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
    /// Classe per gestionar les pistes esportives. Mostra un DataGridView amb les pistes obtingudes de 
    /// l'API. El token de sessió s'envia a través de la capçalera "Session-Token" per autenticar les 
    /// peticions. Permet afegir, modificar i eliminar pistes. Quan es carrega el control, es fa una 
    /// petició GET a l'endpoint "/api/pistes" per obtenir la llista de pistes i mostrar-la al DataGridView. 
    /// Si hi ha un error, es mostra un missatge d'error.
    /// </summary>
    public partial class UCGestCourts : UserControl
    {
        private readonly PistaService _pistaService;

        public UCGestCourts()
        {
            InitializeComponent();
            _pistaService = new PistaService();   
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que es crida quan es carrega el control. Fa una petició GET a l'endpoint "/api/pistes" 
        /// per obtenir la llista de pistes i mostrar-la al DataGridView. Si hi ha un error, es mostra un 
        /// missatge d'error. També s'encarrega d'afegir les columnes d'accions (Modificar i Eliminar) al 
        /// DataGridView. Aquest mètode és asíncron perquè fa una petició a l'API i espera la resposta abans 
        /// de continuar. Si la petició és exitosa, les pistes es mostren al DataGridView. Si hi ha un error, 
        /// es captura l'excepció i es mostra un missatge d'error a l'usuari.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private async void UCGestCourts_Load(object sender, EventArgs e)
        {
            await CarregarPistesAsync();
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per carregar les pistes des de l'API. Utilitza el servei PistaService per fer una petició GET 
        /// a l'endpoint "/api/pistes" i mostrar-les al DataGridView. Si hi ha un error, es mostra un missatge 
        /// d'error. Aquest mètode és asíncron perquè fa una petició a l'API i espera la resposta abans de 
        /// continuar. Si la petició és exitosa, les pistes es mostren al DataGridView. Si hi ha un error, 
        /// es captura l'excepció i es mostra un missatge d'error a l'usuari.
        /// </summary>
        /// <returns>Task que representa l'operació asíncrona.</returns>
        private async Task CarregarPistesAsync()
        {
            try
            {
                var pistes = await _pistaService.GetPistesAsync();
                dataGridViewPistes.DataSource = pistes;
                AfegirColumnesAccions();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error carregant pistes: " + ex.Message);
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per afegir les columnes d'accions (Modificar i Eliminar) al DataGridView. Aquestes columnes 
        /// són de tipus DataGridViewButtonColumn, i cada fila tindrà un botó per modificar i un altre per eliminar 
        /// la pista corresponent. Abans d'afegir les columnes, es comprova si ja existeixen per evitar duplicats 
        /// en cas de refrescar el DataGridView. Si les columnes ja existeixen, el mètode retorna sense fer res. 
        /// Si no existeixen, es creen i s'afegeixen al DataGridView. Aquest mètode és cridat després de 
        /// carregar les pistes per assegurar-se que les columnes d'accions estiguin disponibles per a cada 
        /// fila del DataGridView. Si es fa clic en el botó de modificar o eliminar, s'executaran les accions 
        /// corresponents definides en l'esdeveniment CellContentClick del DataGridView. 
        /// </summary>
        private void AfegirColumnesAccions()
        {
            // Evitar duplicats si refresques
            if (dataGridViewPistes.Columns.Contains("Modificar"))
                return;

            var colModificar = new DataGridViewButtonColumn();
            colModificar.Name = "Modificar";
            colModificar.HeaderText = "";
            colModificar.Text = "Modificar";
            colModificar.UseColumnTextForButtonValue = true;
            colModificar.Width = 90;

            dataGridViewPistes.Columns.Add(colModificar);

            var colEliminar = new DataGridViewButtonColumn();
            colEliminar.Name = "Eliminar";
            colEliminar.HeaderText = "";
            colEliminar.Text = "Eliminar";
            colEliminar.UseColumnTextForButtonValue = true;
            colEliminar.Width = 90;

            dataGridViewPistes.Columns.Add(colEliminar);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que es crida quan es fa clic al botó "Nova Pista". Obre el formulari NewPista per permetre a 
        /// l'usuari introduir les dades d'una nova pista. Després de tancar el formulari, es recarreguen les 
        /// pistes per mostrar la nova pista afegida. Aquest mètode és asíncron perquè després de tancar el 
        /// formulari, es fa una petició a l'API per obtenir la llista actualitzada de pistes i mostrar-la al 
        /// DataGridView. Si la petició és exitosa, les pistes es mostren al DataGridView. Si hi ha un error, 
        /// es captura l'excepció i es mostra un missatge d'error a l'usuari. Aquest procés assegura que el 
        /// DataGridView estigui sempre actualitzat amb les dades més recents de les pistes. 
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private async void btnNewPista_Click(object sender, EventArgs e)
        {
            var form = new NewPista();
            form.ShowDialog();
            await CarregarPistesAsync();
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que es crida quan es fa clic a una cel·la del DataGridView. Gestiona les accions de 
        /// modificar i eliminar pistes. Si es fa clic al botó d'eliminar, es mostra un missatge de 
        /// confirmació i, si l'usuari confirma, es fa una petició DELETE a l'API per eliminar la pista 
        /// seleccionada. Després d'eliminar la pista, es recarreguen les pistes per mostrar la llista 
        /// actualitzada. Si es fa clic al botó de modificar, s'obre el formulari EditPistaForm amb les 
        /// dades de la pista seleccionada. Després de tancar el formulari, es recarreguen les pistes 
        /// per mostrar qualsevol canvi realitzat. Aquest mètode és asíncron perquè després de realitzar 
        /// les accions de modificar o eliminar, es fa una petició a l'API per obtenir la llista actualitzada 
        /// de pistes i mostrar-la al DataGridView. Si la petició és exitosa, les pistes es mostren al 
        /// DataGridView. Si hi ha un error, es captura l'excepció i es mostra un missatge d'error a l'usuari. 
        /// Aquest procés assegura que el DataGridView estigui sempre actualitzat amb les dades més recents 
        /// de les pistes després de qualsevol modificació o eliminació.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private async void dataGridViewPistes_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            // Evitem errors si es clica el header
            if (e.RowIndex < 0)
                return;

            // Comprovar si la columna clicada és la d'Eliminar
            if (dataGridViewPistes.Columns[e.ColumnIndex].Name == "Eliminar")
            {
                var pistaSeleccionada = (Pista)dataGridViewPistes.Rows[e.RowIndex].DataBoundItem;

                var confirm = MessageBox.Show(
                    $"Segur que vols eliminar la pista \"{pistaSeleccionada.name}\"?",
                    "Confirmar eliminació",
                    MessageBoxButtons.YesNo,
                    MessageBoxIcon.Warning
                );

                if (confirm != DialogResult.Yes)
                    return;

                try
                {
                    await _pistaService.DeletePista(pistaSeleccionada.id);

                    MessageBox.Show("Pista eliminada correctament!");

                    // Refrescar el llistat
                    await CarregarPistesAsync();
                    dataGridViewPistes.ClearSelection();

                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message);
                }
            }

            if (dataGridViewPistes.Columns[e.ColumnIndex].Name == "Modificar")
            {
                if (e.RowIndex < 0)
                    return;

                var pistaSeleccionada = (Pista)dataGridViewPistes.Rows[e.RowIndex].DataBoundItem;

                var form = new EditPistaForm(pistaSeleccionada);

                if (form.ShowDialog() == DialogResult.OK)
                {
                    await CarregarPistesAsync();
                    dataGridViewPistes.ClearSelection();
                }
            }

        }
    }
}
