using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SportSpot.Models;


namespace SportSpot.Tests
{
    /// <summary>
    /// Proves unitàries de la classe Session.
    /// Autor: Miquel Uribe Faixedas
    /// </summary>
    [TestClass]
    public class SessionTests
    {
        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Comprova que les propietats de la classe Session es poden assignar i recuperar correctament.
        /// </summary>
        [TestInitialize]
        public void Setup()
        {
            // Neteja abans de cada test
            Session.sessionToken = null;
            Session.role = null;
            Session.user = null;
            Session.email = null;
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Comprova que s'assigna i recupera correctament el sessionToken de la classe Session.
        /// </summary>
        [TestMethod]
        public void AssignarSessionToken_GuardaCorrectament()
        {
            Session.sessionToken = "ABC123";
            Assert.AreEqual("ABC123", Session.sessionToken);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Comprova que s'assigna i recupera correctament el nom d'usuari de la classe Session.
        /// </summary>
        [TestMethod]
        public void AssignarUsuari_GuardaCorrectament()
        {
            Session.user = "Miki";
            Assert.AreEqual("Miki", Session.user);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Comprova que s'assigna i recupera correctament el correu electrònic de la classe Session.
        /// </summary>
        [TestMethod]
        public void AssignarEmail_GuardaCorrectament()
        {
            Session.email = "miki@example.com";
            Assert.AreEqual("miki@example.com", Session.email);
        }

        /// <summary>
        /// Autor: Miquel Uribe Faixedas
        /// Comprova que s'assigna i recupera correctament el rol de la classe Session.
        /// </summary>
        [TestMethod]
        public void AssignarRol_GuardaCorrectament()
        {
            Session.role = "admin";
            Assert.AreEqual("admin", Session.role);
        }
    }
}
