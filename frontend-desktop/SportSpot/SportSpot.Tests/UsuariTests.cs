using SportSpot.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SportSpot.Tests
{
    /// <summary>
    /// Proves unitaries de la classe Usuari.
    /// Autor: Miquel Uribe Faixedas
    /// </summary>
    [TestClass]
    public class UsuariTests
    {
        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Comprova que les propietats de la classe Usuari es poden assignar i recuperar correctament.
        /// </summary>
        [TestMethod]
        public void CrearUsuari_AssignaPropietatsCorrectament()
        {
            var u = new Usuari
            {
                id = 1,
                name = "Miki",
                password = "1234",
                email = "miki@example.com",
                role = "user",
                active = true
            };

            Assert.AreEqual(1, u.id);
            Assert.AreEqual("Miki", u.name);
            Assert.AreEqual("1234", u.password);
            Assert.AreEqual("miki@example.com", u.email);
            Assert.AreEqual("user", u.role);
            Assert.IsTrue(u.active);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Comprova que es poden modificar les propietats d'un usuari després de la seva creació i que els canvis es reflecteixen correctament.
        /// </summary>
        [TestMethod]
        public void ModificarPropietats_DeUsuari()
        {
            var u = new Usuari();

            u.name = "Inicial";
            u.name = "Modificat";

            Assert.AreEqual("Modificat", u.name);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Comprova que es pot canviar l'estat d'activitat d'un usuari i que el valor es reflecteix correctament.
        /// </summary>
        [TestMethod]
        public void AssignarActive_FuncionaCorrectament()
        {
            var u = new Usuari();
            u.active = false;

            Assert.IsFalse(u.active);

            u.active = true;

            Assert.IsTrue(u.active);
        }
    }
}
