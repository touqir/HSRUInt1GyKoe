package application.controller;

import javax.swing.RowSorter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.core.Repository;
import application.presentationModel.componentModel.BookTableModel;
import domain.Book;

public class BookMasterController extends ControllerBase {
    private final Logger logger = LoggerFactory.getLogger(BookMasterController.class);

    public void openBooks(int[] selectedIndices) {
        BookTableModel bookTableModel = getRepository().getBooksPMod().getBookTableModel();
        RowSorter<BookTableModel> sorter = getRepository().getBooksPMod().getBookTableRowSorter();

        for (int index : selectedIndices) {
            Book book = bookTableModel.getBook(sorter.convertRowIndexToModel(index));
            logger.debug("opening book {}", book.getName());
            Repository.getInstance().getMainViewFactory().getBookDetailMainView(book);
        }
    }

    public void searchBooks(String filter) {
        getRepository().getBooksPMod().setSearchString(filter);
    }

    public void setSearchFilter(int columnIndex) {
        getRepository().getBooksPMod().setFilterColumn(columnIndex);
    }

    public void setSearchOnlyAvailableBooks(boolean onlyAvailableBooks) {
        getRepository().getBooksPMod().setOnlyAvailableBooks(onlyAvailableBooks);
    }

    public void openNewBook() {
        Repository.getInstance().getMainViewFactory().getNewBookDetailMainView();
    }

}
