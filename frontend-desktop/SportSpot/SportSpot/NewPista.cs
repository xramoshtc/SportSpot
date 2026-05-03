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
    public partial class NewPista : Form
    {
        private readonly PistaService _pistaService;
        public NewPista()
        {
            InitializeComponent();
            _pistaService = new PistaService();
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que esborra el missatge d'error del nom de la pista quan l'usuari comença a escriure al camp 
        /// de text del nom. Aquest mètode s'executa cada vegada que el contingut del camp de text del nom canvia,
        /// i serveix per proporcionar una millor experiència d'usuari, ja que elimina el missatge d'error 
        /// tan bon punt l'usuari comença a corregir el problema.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private void txtName_TextChanged(object sender, EventArgs e)
        {
            lblErrorName.Text = "";
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que valida el camp de text del nom de la pista quan l'usuari surt del camp (esdeveniment 
        /// Leave). Si el camp està buit o només conté espais en blanc, es mostra un missatge d'error indicant 
        /// que el nom de la pista no pot estar buit. Aquesta validació és important per assegurar que 
        /// l'usuari introdueix un nom vàlid per a la pista, i per evitar problemes en el backend o en la 
        /// base de dades a causa de noms de pistes no vàlids. 
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private void txtName_Leave(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtName.Text))
            {
                lblErrorName.Text = "El nom de la pista no pot estar buit.";
            }
            else
            {
                lblErrorName.Text = "";
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que esborra el missatge d'error de la ubicació de la pista quan l'usuari comença a escriure 
        /// al camp de text de la ubicació. Aquest mètode s'executa cada vegada que el contingut del camp de 
        /// text de la ubicació canvia, i serveix per proporcionar una millor experiència d'usuari, ja que 
        /// elimina el missatge d'error tan bon punt l'usuari comença a corregir el problema. És important tenir 
        /// aquest tipus de validacions i feedback visual per ajudar els usuaris a corregir els errors de manera 
        /// ràpida i eficient, millorant així la usabilitat de l'aplicació.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private void txtLocation_TextChanged(object sender, EventArgs e)
        {
            lblErrorLocation.Text = "";
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètodes que valida el camp de text de la ubicació de la pista quan l'usuari surt del camp 
        /// (esdeveniment Leave).
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private void txtLocation_Leave(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtLocation.Text))
            {
                lblErrorLocation.Text = "La ubicació de la pista no pot estar buida.";
            }
            else
            {
                lblErrorLocation.Text = "";
            }
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode per validar que els camps obligatoris del formulari (nom i ubicació) no estiguin buits 
        /// o només continguin espais en blanc.
        /// </summary>
        /// <returns>Valor boleà que indica si el formulari és vàlid.</returns>
        private bool validateForm()
        {
            return !string.IsNullOrWhiteSpace(txtName.Text) && !string.IsNullOrWhiteSpace(txtLocation.Text);
                   
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que gestiona l'esdeveniment de clic del botó "Crear Pista". Aquest mètode realitza diverses 
        /// accions: valida que s'hagi seleccionat un tipus de pista, valida els camps obligatoris del formulari, 
        /// crea un objecte Pista amb les dades introduïdes per l'usuari, i crida al servei per crear la pista 
        /// a la base de dades. Si la creació és exitosa, es mostra un missatge de confirmació i es tanca el 
        /// formulari. Si hi ha algun error durant el procés, es mostra un missatge d'error adequat. Aquest 
        /// mètode és crucial per assegurar que només es creen pistes vàlides i que els usuaris reben feedback 
        /// adequat sobre les seves accions.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment</param>
        private async void btnCrearPista_Click(object sender, EventArgs e)
        {
            lblError.Text = "";
            lblErrorType.Text = "";

            if (CboxType.SelectedIndex == -1)
            {
                lblErrorType.Text = "Selecciona un tipus de pista.";
                return;
            }
            else if (!validateForm())
            {
                lblError.Text = "Revisa els errors del formulari.";
                return;
            }

            // CREAR OBJECTE (només per passar-lo al servei)
            var pista = new Pista
            {
                name = txtName.Text.Trim(),
                type = CboxType.SelectedItem.ToString(),
                pricePerHour = NumPrice.Value,
                location = txtLocation.Text.Trim()
            };

            try
            {
                // CRIDA AL SERVEI
                var creada = await _pistaService.CreatePista(
                    pista.name,
                    pista.type,
                    pista.pricePerHour,
                    pista.location
                );


                if (creada == null)
                {
                    lblError.Text = "Error creant la pista.";
                    return;
                }

                MessageBox.Show("Pista creada correctament!");

                this.DialogResult = DialogResult.OK;
                this.Close();
            }
            catch (Exception ex)
            {
                lblError.Text = ex.Message;
            }
        }



        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Mètode que esborra el missatge d'error del tipus de pista quan l'usuari selecciona una opció diferent
        /// a la caixa de selecció del tipus de pista.
        /// </summary>
        /// <param name="sender">Objecte que envia l'esdeveniment.</param>
        /// <param name="e">Arguments de l'esdeveniment.</param>
        private void CboxType_SelectedIndexChanged(object sender, EventArgs e)
        {
            lblErrorType.Text = "";
        }
    }
}

