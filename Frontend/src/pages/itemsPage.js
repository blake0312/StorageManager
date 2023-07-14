import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import StorageClient from "../api/storageClient";

class ItemsPage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['render', 'handleToggle'], this);
        this.dataStore = new DataStore();
        this.client = new StorageClient(); // Instantiate the StorageClient
    }

    async mount() {
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
            <div class = "card">
            <div id = "ID">ID: ${item.id}</div>
            <div>Name: ${item.name}</div>
            <div>Value: $${item.value}</div>
            <div>Status: ${item.status}</div>
            <div>Description: ${item.description}</div>
            <div>Quantity: ${item.quantity}</div>
            <div>In Storage: ${item.inStorage}</div>
            <div>Storage Location: ${item.storageLocation}</div>
            <div>Usage Count: ${item.usageCount}</div>
            <div>
            <input type="checkbox"
            data-id="${item.id}" 
            data-name="${item.name}" 
            data-value="${item.value}" 
            data-status="${item.status}" 
            data-description="${item.description}" 
            data-quantity="${item.quantity}"
            data-storageLocation="${item.storageLocation}"
            data-usageCount="${item.usageCount}"
            ${item.inStorage ? 'checked' : ''}>
            
            In Storage
            </div>
            </div>
            
          `
                )
                .join("");

            const checkboxes = resultArea.querySelectorAll('input[type="checkbox"]');
            checkboxes.forEach((checkbox) => {
                checkbox.addEventListener('change', this.handleToggle.bind(this));
            });
        } else {
            resultArea.innerHTML = "No Items";
        }
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

    async handleToggle(event) {
        const itemId = event.target.dataset.id;
        const name = event.target.dataset.name;
        const value = event.target.dataset.value;
        const status = event.target.dataset.status;
        const description = event.target.dataset.description;
        const quantity = event.target.dataset.quantity;
        const storageLocation = event.target.dataset.storagelocation;
        const usageCount = event.target.dataset.usagecount;
        const inStorage = event.target.checked;

        try {
            await this.client.updateItem(itemId,name,value,status,description,quantity,inStorage,storageLocation,usageCount, this.errorHandler);
            const items = await this.client.getAll();
            this.dataStore.setItems(items);
            this.render();
        } catch (error) {
            console.error("Error updating item:", error);
        }
    }


}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const itemsPage = new ItemsPage();
    itemsPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
