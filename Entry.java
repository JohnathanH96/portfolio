public class Entry implements Comparable<Entry> {		//Extends comparable, overwrite compare method

	
	Pair [] fields;
	int count;
	int longestLineLength = 0;
	int fieldCount = 0;
	int authorCount = 0;
	String longestLine;
	String name = "";
	String order;
	String nameRevised;
	String title;
	int year;
     String type;
	 String authors;
	 String volume;
	 String issn;
	 String doi;
	 String url;
	 String pages;
	 String month;
	 String number;
	 String urlDate;
	 String titleRevised;
	
    public Entry(String nameRevised)
    {
        this.nameRevised = nameRevised;
    }

public Entry()
{
	fields = new Pair[25];
}

public void add(Pair p)
{
	if(p == null)
	{
		return;
	}
	fields[count] = p;
	count++;
}

public Pair returnPair() 
{
	Pair s = null;

	for(int i = 0; i < fields.length; i++)
	{
		s = fields[i];
	}
	return s;
}

public int getFieldCount()
{
	return this.fieldCount;
}

@Override
public String toString()
{
            String str = "";
	for (int i = 1; i < count; i++) {
		str = str + "	" + fields[i].type + " = " + fields[i].value + "\n";
	}
	return str;
}

public String getValues()
{
	String str = "{Fields: " + (this.getFieldCount()) + ", Authors: " + getAuthorCount() + ", Longest Field: " + getLongestLine() + ", Length of Longest Field: " + getLongestLineLength() + "}\n}\n";
	return str;
}

public int compareTo(Entry entry) {
	// TODO Auto-generated method stub
	return this.getNameRevised().toLowerCase().compareTo(entry.getNameRevised().toLowerCase());
}

public String printEntry()
{
	String str = "";
	for (int i = 1; i < count; i++) {
		str = str + "\t"+fields[i].type + " = " + fields[i].value + "\n";
	}
	return str;
}

public String getLongestLine() {
	return longestLine;
}

public void setLongestLine(String longestLine) {
	this.longestLine = longestLine;
}

public String getAuthors() {
	return authors;
}

public void setAuthors(String authors) {
	this.authors = authors;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getNameRevised() {
	return nameRevised;
}

public void setNameRevised(String nameRevised) {
	this.nameRevised = nameRevised;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public void setFieldCount(int fieldCount) {
	this.fieldCount = fieldCount;
}

public int getLongestLineLength() {
	return longestLineLength;
}

public void setLongestLineLength(int longestLineLength) {
	this.longestLineLength = longestLineLength;
}

public int getAuthorCount() {
	return authorCount;
}

public void setAuthorCount(int authorCount) {
	this.authorCount = authorCount;
}

public void setYear(int year) {
	// TODO Auto-generated method stub
	this.year = year;
}
public int getYear()
{
	return this.year;
}

public String getVolume() {
	return volume;
}

public void setVolume(String volume) {
	this.volume = volume;
}

public String getIssn() {
	return issn;
}

public void setIssn(String issn) {
	this.issn = issn;
}

public String getDoi() {
	return doi;
}

public void setDoi(String doi) {
	this.doi = doi;
}

public String getUrl() {
	return url;
}

public void setUrl(String url) {
	this.url = url;
}

public String getPages() {
	return pages;
}

public void setPages(String pages) {
	this.pages = pages;
}

public String getMonth() {
	return month;
}

public void setMonth(String month) {
	this.month = month;
}

public String getNumber() {
	return number;
}

public void setNumber(String number) {
	this.number = number;
}

public String getUrlDate() {
	return urlDate;
}

public void setUrlDate(String urlDate) {
	this.urlDate = urlDate;
}

public String getTitleRevised() {
	return titleRevised;
}

public void setTitleRevised(String titleRevised) {
	this.titleRevised = titleRevised;
}
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getType()
    {
        return this.type;
    }


}