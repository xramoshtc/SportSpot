using Microsoft.VisualStudio.TestTools.UnitTesting;
using SportSpot.Models;
using SportSpot.Services;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;

namespace SportSpot.Tests
{
    [TestClass]
    public class PistaServiceTests
    {
        // Handler simulat per interceptar peticions HTTP
        public class FakeHttpMessageHandler : HttpMessageHandler
        {
            private readonly HttpResponseMessage _fakeResponse;

            public FakeHttpMessageHandler(HttpResponseMessage response)
            {
                _fakeResponse = response;
            }

            protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
            {
                return Task.FromResult(_fakeResponse);
            }
        }

        [TestMethod]
        public async Task GetPistesAsync_ReturnsListOfPistes()
        {
            var pistes = new List<Pista>
            {
                new Pista { id = 1, name = "Pista 1" },
                new Pista { id = 2, name = "Pista 2" }
            };

            var fakeResponse = new HttpResponseMessage(HttpStatusCode.OK)
            {
                Content = new StringContent(JsonSerializer.Serialize(pistes))
            };

            var fakeClient = new HttpClient(new FakeHttpMessageHandler(fakeResponse))
            {
                BaseAddress = new System.Uri("https://fakeapi.test/")
            };

            var service = new PistaService(fakeClient);

            var result = await service.GetPistesAsync();

            Assert.AreEqual(2, result.Count);
            Assert.AreEqual("Pista 1", result[0].name);
        }

        [TestMethod]
        public async Task CreatePista_ReturnsCreatedPista()
        {
            var pista = new Pista { id = 10, name = "Nova Pista" };

            var fakeResponse = new HttpResponseMessage(HttpStatusCode.Created)
            {
                Content = new StringContent(JsonSerializer.Serialize(pista))
            };

            var fakeClient = new HttpClient(new FakeHttpMessageHandler(fakeResponse))
            {
                BaseAddress = new System.Uri("https://fakeapi.test/")
            };

            var service = new PistaService(fakeClient);

            var result = await service.CreatePista("Nova Pista", "Padel", 12.5m, "Girona");

            Assert.AreEqual(10, result.id);
            Assert.AreEqual("Nova Pista", result.name);
        }

        [TestMethod]
        public async Task DeletePista_NoContent_ReturnsOk()
        {
            var fakeResponse = new HttpResponseMessage(HttpStatusCode.NoContent);

            var fakeClient = new HttpClient(new FakeHttpMessageHandler(fakeResponse))
            {
                BaseAddress = new System.Uri("https://fakeapi.test/")
            };

            var service = new PistaService(fakeClient);

            await service.DeletePista(1);

            Assert.IsTrue(true);
        }
    }
}
