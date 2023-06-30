import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import StorageClient from "../api/storageClient";

class ItemsPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['render'], this);
        this.dataStore = new DataStore();
    }


    async mount() {
        this.client = new StorageClient();

        const items = await this.client.getAll();
        this.dataStore.setItems(items);
        this.render();
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async render() {
      let resultArea = document.getElementById("Items-info");

      const items = this.dataStore.getItems();

      if (items && items.length > 0) {
        resultArea.innerHTML = items
          .map(
            (item) => `
              <div>ID: ${item.id}</div>
              <div>Name: ${item.name}</div>
              <div>Value: $${item.value}</div>
              <div>Status: ${item.status}</div>
              <div>Description: ${item.description}</div>
              <div>Quantity: ${item.quantity}</div>
              <div>InStorage: ${item.inStorage}</div>
              <div>StorageLocation: ${item.storageLocation}</div>
              <hr>
            `
          )
          .join("");
      } else {
        resultArea.innerHTML = "No Items";
      }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------


}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const storagePage = new StoragePage();
    storagePage.mount();
};

window.addEventListener('DOMContentLoaded', main);
