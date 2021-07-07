package groutboy;

public interface DrawNotifier{
	public void registerDrawListener(DrawListener dl);
	
	public void notifyListeners();
}
