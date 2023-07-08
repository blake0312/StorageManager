document.addEventListener('DOMContentLoaded', function() {
    const manageLink = document.getElementById('manage-link');
    const formsContainer = document.getElementById('forms-container');


    // Add event listener to the manage link
    manageLink.addEventListener('click', function(event) {
        event.preventDefault();
        // Toggle the visibility of the forms container
        formsContainer.style.display = formsContainer.style.display === 'none' ? 'flex' : 'none';
    });
});