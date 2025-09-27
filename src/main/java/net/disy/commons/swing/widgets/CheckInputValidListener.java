package net.disy.commons.swing.events;

import java.awt.datatransfer.FlavorEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TreeSelectionEvent;

public class CheckInputValidListener implements ICheckInputValidListener {
   private final IInputValidCheckable checkable;

   public CheckInputValidListener(IInputValidCheckable checkable) {
      this.checkable = checkable;
   }

   @Override
   public void checkInputValid() {
      this.checkable.checkInputValid();
   }

   @Override
   public void itemStateChanged(ItemEvent e) {
      this.checkInputValid();
   }

   @Override
   public void valueChanged(ListSelectionEvent e) {
      this.checkInputValid();
   }

   @Override
   public void insertUpdate(DocumentEvent e) {
      this.checkInputValid();
   }

   @Override
   public void removeUpdate(DocumentEvent e) {
      this.checkInputValid();
   }

   @Override
   public void changedUpdate(DocumentEvent e) {
      this.checkInputValid();
   }

   @Override
   public void stateChanged() {
      this.checkInputValid();
   }

   @Override
   public void tableChanged(TableModelEvent e) {
      this.checkInputValid();
   }

   @Override
   public void propertyChange(PropertyChangeEvent evt) {
      this.checkInputValid();
   }

   @Override
   public void valueChanged(TreeSelectionEvent e) {
      this.checkInputValid();
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      this.checkInputValid();
   }

   @Override
   public void stateChanged(ChangeEvent e) {
      this.checkInputValid();
   }

   @Override
   public void flavorsChanged(FlavorEvent e) {
      this.checkInputValid();
   }

   @Override
   public void contentsChanged(ListDataEvent e) {
      this.checkInputValid();
   }

   @Override
   public void intervalAdded(ListDataEvent e) {
      this.checkInputValid();
   }

   @Override
   public void intervalRemoved(ListDataEvent e) {
      this.checkInputValid();
   }
}
