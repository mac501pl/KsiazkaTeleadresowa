$(document).ready(() => {
    const contacts = JSON.parse(HtmlAndroidBridge.getContacts());

    contacts.forEach(contact => {
        $("table tr:last").after(`<tr class="item"><td>${contact.firstName}</td><td>${contact.lastName}</td><td>${contact.phoneNumber}</td><td><button data-number="${contact.phoneNumber}" class="more-info-button">...</button></td></tr>`);
    });

    $(document).on("click", ".more-info-button", e => {
        const clickedContact = contacts.find(contact => contact.phoneNumber == $(e.target).data("number"));

        $(".firstName").text(clickedContact.firstName);
        $(".lastName").text(clickedContact.lastName);
        $(".phoneNumber").text(clickedContact.phoneNumber);
        $(".email").text(clickedContact.email);
        $(".company").text(clickedContact.company);
        $(".title").text(clickedContact.title);
        $(".notes").text(clickedContact.notes);
    });
});