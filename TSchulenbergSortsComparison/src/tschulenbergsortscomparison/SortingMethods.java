package tschulenbergsortscomparison;

import java.util.Random;

public class SortingMethods {
	String[] data;

	public void RandomStrings(int Length) {
		Random r = new Random();
		int num = 0;
		StringBuilder p = new StringBuilder();
		data = new String[Length];
		for (int i = 0; i < Length; i++) { // loop creates random strings

			num = 65 + r.nextInt(24);
			char a = (char) num;
			num = 65 + r.nextInt(24);
			char b = (char) num;
			num = 65 + r.nextInt(24);
			char c = (char) num;
			num = 65 + r.nextInt(24);
			char d = (char) num;
			num = 65 + r.nextInt(24);
			char e = (char) num;
			p.append(a).append(b).append(c).append(d).append(e);
			String q = p.toString();
			data[i] = q;

			p.delete(0, p.length());

		}
	}

	public void InsertionSort(int firstNdx, int numToSort) {
		String Temp; // String that is being inserted
		int Q; // Index of first element in unsorted array
		int W; // Index into sorted position of array
		int E; // Index for shifting elements

		for (Q = 1; Q < numToSort; Q++) {
			Temp = data[firstNdx + Q];
			// locate insert point of temp within sorted position
			// of array starting at the rear of the sorted portion
			for (W = firstNdx + Q; W > firstNdx; W--) {
				if (Temp.compareTo(data[W - 1]) >= 0)
					break;
			}
			for (E = firstNdx + Q; E > W; E--) {
				data[E] = data[E - 1];
			}
			// insert Temp
			data[W] = Temp;
		}
	}

	public void QuickSort(int First, int n) {
		int pivotIndex; // array index for the pivot element
		int n1; // number of elements before the pivot element
		int n2; // number of elements after the pivot element

		if (n > 16) {
			// partition the array, and set the pivot index
			pivotIndex = partition(First, n);
			// compute the sizes of the two pieces
			n1 = pivotIndex - First;
			n2 = n - n1 - 1;
			// recusive calls to sort the two pieces
			QuickSort(First, n1);
			QuickSort(pivotIndex + 1, n2);
		}
		if (n <= 16) {
			InsertionSort(First, n);
		}
	}

	public int RandomPivot(int first, int n) {
		int add = first + n; // sums up first and last
		int Mid = add / 2; // divide by two to make middle
		//int x = first;

		if (data[first].compareTo(data[n - 1]) < 0) {
			if (data[n - 1].compareTo(data[Mid]) < 0) 
				return n - 1; // if n-1 is mediam
			 else 
				if (data[Mid].compareTo(data[first]) > 0) 
					return Mid; // if mid is median
				else 
					return n -1;
			
		} else {
			if (data[first].compareTo(data[Mid]) > 0)
				return first; // if first is median
			else
				return Mid;
		}
	}

	public int partition(int first, int n) {

		int toobigNdx = first + 1; // index of element after pivot
		int toosmallNdx = first + n - 1; // index of last element
		String Temp; // string to hold items during switch
		int x = RandomPivot(first, n); //pivot point
		
		Temp = data[first];
		data[first] = data[x]; // block makes pivot first value
		data[x] = Temp;
		String Pivot = data[first];
		//orgranizes partitions
		while (toobigNdx < toosmallNdx) {
			while (toobigNdx < toosmallNdx
					&& data[toobigNdx].compareTo(Pivot) <= 0) {
				toobigNdx++;
			}
			while (toosmallNdx > first
					&& data[toosmallNdx].compareTo(Pivot) > 0) {
				toosmallNdx--;
			}
			if (toobigNdx < toosmallNdx) {
				Temp = data[toobigNdx];
				data[toobigNdx] = data[toosmallNdx];
				data[toosmallNdx] = Temp;
			}
		}
		//moves pivot to correct position
		if (Pivot.compareTo(data[toosmallNdx]) >= 0) {
			Temp = Pivot;
			Pivot = data[toosmallNdx];
			data[toosmallNdx] = Temp;
			return toosmallNdx;
		} else {
			return first;
		}
	}

	public void mergesort(int first, int n) {
		int n1; // size of first half
		int n2; // size of second half

		if (n > 1) {
			// compute sizes
			n1 = n / 2;
			n2 = n - n1;

			mergesort(first, n1);
			mergesort(first + n1, n2);

			// merge two sorted halves
			merge(first, n1, n2);
		}
	}

	public void merge(int first, int n1, int n2) {
		String[] Temp = new String[n1 + n2];
		int copied = 0;
		int copied1 = 0;
		int copied2 = 0;
		int i;

		// merge elements
		while ((copied1 < n1) && (copied2 < n2)) {
			if (data[first + copied1].compareTo(data[first + n1 + copied2]) < 0)
				Temp[copied++] = data[first + (copied1++)];
			else
				Temp[copied++] = data[first + n1 + (copied2++)];
		}

		// copy remaining entries
		while (copied1 < n1)
			Temp[copied++] = data[first + (copied1++)];
		while (copied2 < n2)
			Temp[copied++] = data[first + n1 + (copied2++)];

		// copy temp back into array
		for (i = 0; i < n1 + n2; i++)
			data[first + i] = Temp[i];
	}
}
