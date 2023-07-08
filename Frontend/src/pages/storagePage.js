import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import StorageClient from "../api/storageClient";

class StoragePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onCreate', 'onRemove'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        document.getElementById('create-form').addEventListener('submit', this.onCreate);
        document.getElementById('remove-form').addEventListener('submit', this.onRemove);

        this.client = new StorageClient();

    }

    // Render Methods --------------------------------------------------------------------------------------------------


    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGet(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let id = document.getElementById("id-field").value;
        this.dataStore.set("item", null);

        let result = await this.client.getItem(id, this.errorHandler);
        this.dataStore.set("item", result);
        if (result) {
            this.showMessage(`Got ${result.name}!`)
        } else {
            this.errorHandler("Error doing GET!  Try again...");
        }
    }

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("item", null);

        let name = document.getElementById("create-name-field").value;
        let value = document.getElementById("create-value-field").value;
        let status = document.getElementById("create-status-field").value;
        let description = document.getElementById("create-description-field").value;
        let quantity = document.getElementById("create-quantity-field").value;
        let inStorage = document.getElementById("create-inStorage-field").checked;
        let storageLocation = document.getElementById("create-storageLocation-field").value;

        const createdItem = await this.client.createItem(name,value,status,description,quantity,inStorage,storageLocation,  this.errorHandler);
        this.dataStore.set("item", createdItem);

        if (createdItem) {
            this.showMessage(`Created ${createdItem.name}!`)
        } else {
            this.errorHandler("Error creating!  Try again...");
        }
    }

    async onRemove(event) {
        event.preventDefault();
        this.dataStore.set("item", null);

        let id = document.getElementById("remove-id-field").value;

        const removedItem = await this.client.removeItem(id, this.errorHandler);
        if (removedItem) {
            this.showMessage(`Removed ${id}!`)
        } else {
            this.errorHandler("Error removing!  Try again...");
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const storagePage = new StoragePage();
    storagePage.mount();
};

window.addEventListener('DOMContentLoaded', main);
