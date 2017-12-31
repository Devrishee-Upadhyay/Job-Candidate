public class Model
{
   private String name="";
   private String description="";
   private String course="";
   private String city="";

     public Model()
	{
	}

	public Model(String name,String description,String course,String city)
	{
		this.name=name;
		this.description=description;
		this.course=course;
		this.city=city;
	}
	public String getName(){ return name;}
	public String getDescription(){ return description;}
	public String getCourse(){return course;}
    public String getCity(){return city;}

    public String toString()
	{
		return name+"\t"+description+"\t"+course+"\t"+city;
	}

}
