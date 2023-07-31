package process.iterator;

import entity.CreditCard;

import java.util.List;
import java.util.NoSuchElementException;

public class RecordsIteratorImpl implements RecordsIterator {
    private List<CreditCard> creditCards;
    private int currentIndex;

    public RecordsIteratorImpl(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < creditCards.size();
    }

    @Override
    public CreditCard next() {
        if (hasNext()) {
            return creditCards.get(currentIndex++);
        } else {
            throw new NoSuchElementException("No more elements in the iterator.");
        }
    }
}